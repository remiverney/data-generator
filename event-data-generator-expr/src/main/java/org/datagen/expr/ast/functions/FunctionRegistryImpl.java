package org.datagen.expr.ast.functions;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.datagen.expr.ast.CastMatrix;
import org.datagen.expr.ast.exception.BadArgumentsNumberException;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.nodes.ArrayDef;
import org.datagen.expr.ast.nodes.LiteralValue;
import org.datagen.utils.EmptyFunction;
import org.datagen.utils.TriFunction;
import org.datagen.utils.VarArgFunction;

public class FunctionRegistryImpl implements FunctionRegistry {

	public interface ParameterBridge {
		Object bridge(Value value);
	}

	public interface CallBridge {
		Value bridge(Node node, List<Value> parameters);
	}

	public static abstract class BaseCallBridge implements CallBridge {

		private final boolean deterministic;

		public BaseCallBridge(boolean deterministic) {
			this.deterministic = deterministic;
		}

		public boolean isDeterministic() {
			return this.deterministic;
		}
	}

	public static class EmptyFunctionCallBridge<R> extends BaseCallBridge {

		private final EmptyFunction<R> function;

		private EmptyFunctionCallBridge(boolean deterministic,
				EmptyFunction<R> function) {
			super(deterministic);
			this.function = function;
		}

		@Override
		public Value bridge(Node node, List<Value> parameters) {
			if (!parameters.isEmpty()) {
				throw new BadArgumentsNumberException(node, parameters.size(),
						0);
			}

			return toValue(function.apply());
		}
	}

	public static class FunctionCallBridge<T, R> extends BaseCallBridge {

		private final Function<T, R> function;
		private final Class<T> clazz1;

		private FunctionCallBridge(boolean deterministic,
				Function<T, R> function, Class<T> clazz1) {
			super(deterministic);
			this.function = function;
			this.clazz1 = clazz1;
		}

		@Override
		public Value bridge(Node node, List<Value> parameters) {
			if (!(parameters.size() == 1)) {
				throw new BadArgumentsNumberException(node, parameters.size(),
						1);
			}

			return toValue(function.apply(fromValue(node, 1, parameters.get(0),
					clazz1)));
		}
	}

	public static class BiFunctionCallBridge<T, U, R> extends BaseCallBridge {

		private final BiFunction<T, U, R> function;
		private final Class<T> clazz1;
		private final Class<U> clazz2;

		private BiFunctionCallBridge(boolean deterministic,
				BiFunction<T, U, R> function, Class<T> clazz1, Class<U> clazz2) {
			super(deterministic);
			this.function = function;
			this.clazz1 = clazz1;
			this.clazz2 = clazz2;
		}

		@Override
		public Value bridge(Node node, List<Value> parameters) {
			if (!(parameters.size() == 2)) {
				throw new BadArgumentsNumberException(node, parameters.size(),
						2);
			}

			return toValue(function.apply(
					fromValue(node, 1, parameters.get(0), clazz1),
					fromValue(node, 2, parameters.get(1), clazz2)));
		}
	}

	public static class TriFunctionCallBridge<T, U, V, R> extends
			BaseCallBridge {

		private final TriFunction<T, U, V, R> function;
		private final Class<T> clazz1;
		private final Class<U> clazz2;
		private final Class<V> clazz3;

		private TriFunctionCallBridge(boolean deterministic,
				TriFunction<T, U, V, R> function, Class<T> clazz1,
				Class<U> clazz2, Class<V> clazz3) {
			super(deterministic);
			this.function = function;
			this.clazz1 = clazz1;
			this.clazz2 = clazz2;
			this.clazz3 = clazz3;
		}

		@Override
		public Value bridge(Node node, List<Value> parameters) {
			if (!(parameters.size() == 3)) {
				throw new BadArgumentsNumberException(node, parameters.size(),
						3);
			}

			return toValue(function.apply(
					fromValue(node, 1, parameters.get(0), clazz1),
					fromValue(node, 2, parameters.get(1), clazz2),
					fromValue(node, 3, parameters.get(2), clazz3)));
		}
	}

	public static class VarArgFunctionCallBridge<T, R> extends BaseCallBridge {

		private final VarArgFunction<T, R> function;
		private final Class<T> clazz1;

		private VarArgFunctionCallBridge(boolean deterministic,
				VarArgFunction<T, R> function, Class<T> clazz1) {
			super(deterministic);
			this.function = function;
			this.clazz1 = clazz1;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Value bridge(Node node, List<Value> parameters) {
			T[] literals = (T[]) Array.newInstance(clazz1, parameters.size());

			int i = 0;

			for (Value value : parameters) {
				literals[i] = fromValue(node, i, value, clazz1);
				i++;
			}

			return toValue(function.apply(literals));
		}
	}

	private final Class<?> FUNCTION_CLASSES[] = new Class<?>[] {
			MathFunctions.class, StringFunctions.class };

	private final Map<String, CallBridge> functions = new HashMap<>();

	public FunctionRegistryImpl() {
		registerAll();
	}

	@Override
	public Object getFunction(String name) {
		return functions.get(name);
	}

	@Override
	public Value invokeFunction(Node node, String name, List<Value> parameters) {
		CallBridge bridge = functions.get(name);

		if (bridge == null) {
			throw new UnresolvedReferenceException(node, name);
		}

		return bridge.bridge(node, parameters);
	}

	private void registerAll() {
		for (Class<?> clazz : FUNCTION_CLASSES) {
			registerAll(clazz);
		}
	}

	private void registerAll(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
					&& java.lang.reflect.Modifier
							.isPublic(field.getModifiers())
					&& java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
				try {
					functions.put(field.getName(), buildBridge(field));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(
							"Failed to register function field '"
									+ field.getName() + "' of class '"
									+ clazz.getName() + "', skipping", e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private CallBridge buildBridge(Field field)
			throws IllegalArgumentException, IllegalAccessException {
		Type type = field.getGenericType();

		if (!(type instanceof ParameterizedType)) {
			throw new RuntimeException("Failed to register function field '"
					+ field.getName() + "' of non parameterized type '"
					+ type.getTypeName() + "', skipping");
		}

		ParameterizedType parameterized = (ParameterizedType) type;
		boolean deterministic = !field.getDeclaringClass().isAnnotationPresent(
				NonDeterministic.class)
				&& !field.isAnnotationPresent(NonDeterministic.class);

		if (parameterized.getRawType() == EmptyFunction.class) {
			return new EmptyFunctionCallBridge<>(deterministic,
					(EmptyFunction<Object>) field.get(null));
		} else if (parameterized.getRawType() == Function.class) {
			return new FunctionCallBridge<>(deterministic,
					(Function<Object, Object>) field.get(null),
					(Class<Object>) parameterized.getActualTypeArguments()[0]);
		} else if (parameterized.getRawType() == BiFunction.class) {
			return new BiFunctionCallBridge<>(deterministic,
					(BiFunction<Object, Object, Object>) field.get(null),
					(Class<Object>) parameterized.getActualTypeArguments()[0],
					(Class<Object>) parameterized.getActualTypeArguments()[1]);
		} else if (parameterized.getRawType() == TriFunction.class) {
			return new TriFunctionCallBridge<>(deterministic,
					(TriFunction<Object, Object, Object, Object>) field
							.get(null),
					(Class<Object>) parameterized.getActualTypeArguments()[0],
					(Class<Object>) parameterized.getActualTypeArguments()[1],
					(Class<Object>) parameterized.getActualTypeArguments()[2]);
		} else if (parameterized.getRawType() == VarArgFunction.class) {
			return new VarArgFunctionCallBridge<>(deterministic,
					(VarArgFunction<Object, Object>) field.get(null),
					(Class<Object>) parameterized.getActualTypeArguments()[0]);
		} else {
			throw new RuntimeException("Failed to register function field '"
					+ field.getName() + "' of non function type '"
					+ type.getTypeName() + "', skipping");
		}
	}

	private static <T> Value toValue(T literal) {
		if (literal.getClass() == Long.class) {
			return new LiteralValue((Long) literal);
		}

		if (literal.getClass() == Integer.class) {
			return new LiteralValue((Integer) literal);
		}

		if (literal.getClass() == Short.class) {
			return new LiteralValue((Short) literal);
		}

		if (literal.getClass() == Byte.class) {
			return new LiteralValue((Byte) literal);
		}

		if (literal.getClass() == Double.class) {
			return new LiteralValue((Double) literal);
		}

		if (literal.getClass() == Float.class) {
			return new LiteralValue((Float) literal);
		}

		if (literal.getClass() == Boolean.class) {
			return new LiteralValue((Boolean) literal);
		}

		if (literal.getClass() == Date.class) {
			return new LiteralValue((Date) literal);
		}

		if (literal.getClass() == String.class) {
			return new LiteralValue((String) literal);
		}

		if (literal.getClass() == Character.class) {
			return new LiteralValue(((Character) literal).toString());
		}

		if (literal.getClass() == char[].class) {
			return new LiteralValue(new String((char[]) literal));
		}

		if (literal.getClass() == byte[].class) {
			return new LiteralValue(new String((byte[]) literal));
		}

		if (literal.getClass().isEnum()) {
			return new LiteralValue(((Enum<?>) literal).name());
		}

		if (literal.getClass().isArray()) {
			return new ArrayDef(Arrays.stream((Object[]) literal)
					.map(x -> toValue(x))
					.collect(Collectors.toCollection(ArrayList::new)));
		}

		throw new IllegalArgumentException(
				"Unexpected literal class to convert to a Value [ "
						+ literal.getClass().getName() + " ]");
	}

	@SuppressWarnings("unchecked")
	private static <T> T fromValue(Node node, int parameter, Value value,
			Class<T> clazz) {
		if (!(value instanceof LiteralValue)) {
			throw new IncompatibleArgumentException(node, parameter,
					value.getType());
		}

		LiteralValue literal = (LiteralValue) value;

		Object result = literal.get();
		if (!clazz.isInstance(result)) {
			result = CastMatrix.cast(result, clazz);
			if (result == null) {
				throw new IncompatibleArgumentException(node, parameter,
						value.getType());
			}
		}

		return (T) result;
	}

}
