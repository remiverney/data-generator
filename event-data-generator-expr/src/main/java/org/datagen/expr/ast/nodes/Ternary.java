package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class Ternary implements Node {

	private final Node condition;
	private final Node then;
	private final Node otherwise;

	public Ternary(Node condition, Node then, Node otherwise) {
		super();
		this.condition = condition;
		this.then = then;
		this.otherwise = otherwise;
	}

	public Node getCondition() {
		return condition;
	}

	public Node getThen() {
		return then;
	}

	public Node getOtherwise() {
		return otherwise;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(condition);
		children.add(then);
		children.add(otherwise);

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.evalBoolean(context, this, condition.eval(context)) ? then.eval(context) : otherwise
				.eval(context);
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		condition.toString(builder, context);
		context.spacing(builder);
		builder.append('?');
		context.spacing(builder);
		then.toString(builder, context);
		context.spacing(builder);
		builder.append(':');
		context.spacing(builder);
		otherwise.toString(builder, context);

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if (condition instanceof LiteralValue) {
			if (((LiteralValue) condition).getType() == ValueType.BOOLEAN) {
				return ((LiteralValue) condition).getBool() ? then : otherwise;
			}
		}

		return Node.super.optimize(context);
	}

}
