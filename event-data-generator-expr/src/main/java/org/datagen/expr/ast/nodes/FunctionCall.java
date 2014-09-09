package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class FunctionCall implements Node {

	private final String name;
	private final List<Node> parameters;

	public FunctionCall(String name, List<Node> parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public List<Node> getParameters() {
		return parameters;
	}

	@Override
	public List<Node> getChildren() {
		return parameters;
	}

	@Override
	public Value eval(EvalContext context) {
		return context.getFunctionRegistry().invokeFunction(
				this,
				name,
				parameters.stream().map(p -> p.eval(context))
						.collect(Collectors.toCollection(ArrayList::new)));
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		builder.append(':').append(name);
		builder.append('(');
		context.formatList(builder, parameters, ',');
		builder.append(')');

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		boolean deterministic = context.getFunctionRegistry().isDeterministic(
				this, name);

		if (deterministic
				&& parameters.stream().allMatch(
						p -> (p instanceof LiteralValue))) {
			return context.getFunctionRegistry().invokeFunction(
					this,
					name,
					parameters.stream().map(p -> p.eval(context))
							.collect(Collectors.toCollection(ArrayList::new)));
		} else {
			return Node.super.optimize(context);
		}
	}
}
