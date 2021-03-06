package org.datagen.expr.ast.nodes;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.FunctionInvocationException;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.exception.IncompatibleFunctionReturnTypeException;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.functions.MethodHelpers;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class JavaRef implements Node {

	private final Class<?> clazz;
	private final String methodName;
	private final String reference;
	private final MethodHandle handle;
	private final Optional<List<Node>> parameters;
	private final Optional<ClassLoader> loader;

	public JavaRef(List<String> fqn) {
		this(fqn, Optional.<ClassLoader> empty());
	}

	public JavaRef(List<String> fqn, ClassLoader loader) {
		this(fqn, Optional.<ClassLoader> of(loader));
	}

	public JavaRef(List<String> fqn, Optional<ClassLoader> loader) {
		this(fqn, Optional.<List<Node>> empty(), loader);
	}

	public JavaRef(List<String> fqn, List<Node> parameters) {
		this(fqn, Optional.<List<Node>> of(parameters), Optional.<ClassLoader> empty());
	}

	public JavaRef(List<String> fqn, Optional<List<Node>> parameters, Optional<ClassLoader> loader) {
		this.loader = loader;
		this.methodName = fqn.remove(fqn.size() - 1);
		this.clazz = findClass(fqn);
		this.parameters = parameters;
		this.reference = clazz.getName() + "." + methodName;
		this.handle = resolveHandle();
	}

	public JavaRef(String fqn) {
		this(fqn, Optional.<ClassLoader> empty());
	}

	public JavaRef(String fqn, ClassLoader loader) {
		this(fqn, Optional.<ClassLoader> of(loader));
	}

	public JavaRef(String fqn, Optional<ClassLoader> loader) {
		this(fqn, Optional.empty(), loader);
	}

	public JavaRef(String fqn, List<Node> parameters) {
		this(fqn, Optional.<List<Node>> of(parameters), Optional.<ClassLoader> empty());
	}

	public JavaRef(String fqn, Optional<List<Node>> parameters, Optional<ClassLoader> loader) {
		this.loader = loader;
		int pos = fqn.lastIndexOf('.');

		if (pos <= 0) {
			throw new UnresolvedReferenceException(this, fqn);
		}

		this.methodName = fqn.substring(pos + 1);
		this.clazz = findClass(fqn.substring(0, pos));
		this.parameters = parameters;
		this.reference = clazz.getName() + "." + methodName;
		this.handle = resolveHandle();
	}

	private boolean isMethodRef() {
		return parameters.isPresent();
	}

	private Class<?> findClass(List<String> fqn) {
		return findClass(fqn.stream().collect(Collectors.joining(".")));
	}

	private Class<?> findClass(String className) {
		try {
			return (!this.loader.isPresent()) ? Class.forName(className) : Class.forName(className, true,
					this.loader.get());
		} catch (ClassNotFoundException e) {
			throw new UnresolvedReferenceException(this, className);
		}
	}

	private MethodHandle resolveHandle() {
		if (isMethodRef()) {
			Method methods[] = clazz.getDeclaredMethods();

			Optional<Method> vararg = Optional.empty();

			for (Method method : methods) {
				if (!method.getName().equals(methodName)) {
					continue;
				}

				int flags = method.getModifiers();

				if (Modifier.isAbstract(flags) || Modifier.isPrivate(flags) || Modifier.isProtected(flags)
						|| !Modifier.isStatic(flags) || Modifier.isAbstract(flags)) {
					continue;
				}

				if ((method.getParameterCount() - 1 <= parameters.get().size()) && (method.isVarArgs())) {
					vararg = Optional.of(method);
					continue;
				} else if (method.getParameterCount() != parameters.get().size()) {
					continue;
				}

				try {
					return MethodHandles.publicLookup().unreflect(method);
				} catch (IllegalAccessException e) {
					continue;
				}
			}

			if (vararg.isPresent()) {
				try {
					return MethodHandles.publicLookup().unreflect(vararg.get());
				} catch (IllegalAccessException e) {
				}
			}

			throw new UnresolvedReferenceException(this, reference);
		} else {
			try {
				Field field = clazz.getDeclaredField(methodName);
				return MethodHandles.publicLookup().findStaticGetter(clazz, methodName, field.getType());
			} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
				throw new UnresolvedReferenceException(this, reference, e);
			}
		}
	}

	@Override
	public List<Node> getChildren() {
		return isMethodRef() ? parameters.get() : Node.super.getChildren();
	}

	@Override
	public Value eval(EvalContext context) {
		return isMethodRef() ? evalMethod(context) : evalField(context);
	}

	private Value evalMethod(EvalContext context) {
		assert isMethodRef() : "unexpected call in case of field reference";

		List<Object> args = parameters.get().stream().sequential().map(p -> getLiteral(p.eval(context), 0))
				.collect(Collectors.toList());

		try {
			Object result = handle.invokeWithArguments(args);
			return MethodHelpers.toLiteralValue(result);
		} catch (Throwable t) {
			throw new FunctionInvocationException(this, reference, t);
		}
	}

	private Value evalField(EvalContext context) {
		assert !isMethodRef() : "unexpected call in case of method reference";

		try {
			Object result = handle.invoke();
			LiteralValue value = MethodHelpers.toLiteralValue(result);
			if (value == null) {
				throw new IncompatibleFunctionReturnTypeException(this, reference, result.getClass());
			}

			return value;
		} catch (Throwable t) {
			throw new FunctionInvocationException(this, reference, t);
		}
	}

	private Object getLiteral(Value value, int parameter) {
		if (value.isLiteral()) {
			return ((LiteralValue) value).get();
		} else if (value.isArray()) {
			return ((Array) value).getAll().toArray();
		} else {
			throw new IncompatibleArgumentException(this, parameter, value.getType());
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		builder.append("java:");
		builder.append(reference);

		if (parameters.isPresent()) {
			builder.append('(');
			context.formatList(builder, parameters.get(), ',');
			builder.append(')');
		}

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if (isMethodRef()) {
			return Node.super.optimize(context);
		} else {
			return evalField(context);
		}
	}
}
