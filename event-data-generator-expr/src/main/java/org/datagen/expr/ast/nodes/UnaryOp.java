package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;

public abstract class UnaryOp<O extends Operator<O>> implements Node {

	protected final O operator;
	protected final Node rhs;
	protected final boolean prefixed;

	protected UnaryOp(O operator, Node rhs) {
		this.operator = operator;
		this.rhs = rhs;
		this.prefixed = true;
	}

	protected UnaryOp(O operator, Node rhs, boolean prefixed) {
		this.operator = operator;
		this.rhs = rhs;
		this.prefixed = prefixed;
	}

	public O getOperator() {
		return operator;
	}

	public Node getRhs() {
		return rhs;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(rhs);

		return children;
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		if (prefixed) {
			builder.append(operator.getSymbol());
		}

		if (rhs instanceof BinaryOp) {
			builder.append('(');
			rhs.toString(builder, context);
			builder.append(')');
		} else {
			rhs.toString(builder, context);
		}

		if (!prefixed) {
			builder.append(operator.getSymbol());
		}

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if (rhs instanceof LiteralValue) {
			return eval(context);
		} else {
			return Node.super.optimize(context);
		}
	}
}
