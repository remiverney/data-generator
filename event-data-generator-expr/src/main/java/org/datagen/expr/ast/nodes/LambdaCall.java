package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.NotALambdaException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class LambdaCall implements Node {

	private final Node lambda;
	private final List<Node> parameters;

	public LambdaCall(Node lambda, List<Node> parameters) {
		this.lambda = lambda;
		this.parameters = parameters;
	}

	public Node getLambda() {
		return lambda;
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
		Value resolved = lambda.eval(context);

		if (!resolved.isLambda()) {
			throw new NotALambdaException(this, resolved.getType());
		}

		return ((Lambda) resolved).eval(context,
				parameters.stream().map(p -> p.eval(context)).collect(Collectors.toCollection(ArrayList::new)));
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		lambda.toString(builder, context);
		builder.append('(');
		context.formatList(builder, parameters, ',');
		builder.append(')');

		return builder;
	}
}
