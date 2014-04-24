package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.datagen.expr.ast.AstWalker;
import org.datagen.expr.ast.DefaultValueFormatContext;
import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.ValueFormatContext;
import org.datagen.expr.ast.exception.BadArgumentsNumberException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class LambdaDef implements Value, Lambda {

	private final List<String> parameters;
	private final Node body;

	public LambdaDef(List<String> parameters, Node body) {
		this.parameters = parameters;
		this.body = body;
	}

	public List<String> getParameters() {
		return parameters;
	}

	@Override
	public Node getBody() {
		return body;
	}

	@Override
	public List<Node> getChildren() {
		return Collections.<Node> singletonList(body);
	}

	@Override
	public Value eval(EvalContext context) {
		return new LambdaDef(parameters, body) {
			private final Map<String, Value> closure = defineClosure(context);

			@Override
			public Value eval(EvalContext context) {
				return super.eval(context);
			}

			@Override
			public Value eval(EvalContext context, Value... parameters) {
				if (parameters.length != LambdaDef.this.parameters.size()) {
					throw new BadArgumentsNumberException(this,
							parameters.length, LambdaDef.this.parameters.size());
				}

				Value values[] = new Value[LambdaDef.this.parameters.size()];

				for (int i = 0; i < LambdaDef.this.parameters.size(); i++) {
					values[i] = parameters[i].eval(context);
				}

				context.pushContext();

				context.setVariable(ThisRef.THIS_VARIABLE, this);
				for (int i = 0; i < LambdaDef.this.parameters.size(); i++) {
					context.setVariable(LambdaDef.this.parameters.get(i),
							values[i]);
				}
				context.pushContext(closure);

				Value result = body.eval(context);

				context.popContext();
				context.popContext();

				return result;
			}

		};
	}

	@Override
	public boolean isLambda() {
		return true;
	}

	@Override
	public int getArity() {
		return parameters.size();
	}

	@Override
	public ValueType getType() {
		return ValueType.LAMBDA;
	}

	@Override
	public Value eval(EvalContext context, Value... parameters) {
		throw new UnsupportedOperationException(
				"attempted to call a lambda that was not evaluated before");
	}

	@Override
	public Value eval(EvalContext context, Collection<Value> parameters) {
		return eval(context, parameters.toArray(new Value[parameters.size()]));
	}

	@Override
	public String toValueString(ValueFormatContext context) {
		return context.formatLambda(this);
	}

	private Map<String, Value> defineClosure(EvalContext context) {
		List<String> references = new ArrayList<>();

		System.out.println("define closure:");

		Visitor<VariableRef> visitor = new Visitor<VariableRef>() {
			@Override
			public VariableRef visit(VariableRef visited) {
				if (!parameters.contains(visited.getReference())) {
					references.add(visited.getReference());
					System.out.println("   add ref " + visited.getReference());
				}
				return visited;
			}
		};

		AstWalker.walk(this, VariableRef.class, visitor, true);

		Map<String, Value> closure = references
				.stream()
				.filter(x -> context.getVariable(x) != null)
				.collect(
						Collectors.toMap(Function.<String> identity(),
								x -> context.getVariable(x)));
		System.out.println("resolved closure:");

		for (Map.Entry<String, Value> entry : closure.entrySet()) {
			System.out.println("   "
					+ entry.getKey()
					+ "="
					+ entry.getValue().toValueString(
							new DefaultValueFormatContext()));
		}
		return closure;
	}
}
