package org.datagen.expr.ast.nodes;

import java.util.List;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public abstract class BinaryOp<O extends Enum<O> & Operator<O>> extends UnaryOp<O> {

	protected final Node lhs;

	protected BinaryOp(Node lhs, Node rhs, O operator) {
		super(operator, rhs);
		this.lhs = lhs;
	}

	public Node getLhs() {
		return lhs;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(0, lhs);

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		Value lvalue = lhs.eval(context);
		Value rvalue = rhs.eval(context);

		try {
			return operator.getEvaluator().eval(context, this, lvalue, rvalue);
		} catch (ArithmeticException e) {
			throw new org.datagen.expr.ast.exception.ArithmeticException(this, operator, lvalue, rvalue, e);
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		if ((lhs instanceof BinaryOp)
				&& ((BinaryOp<?>) lhs).getOperator().getPrecedence().less(super.getOperator().getPrecedence())) {
			builder.append('(');
			lhs.toString(builder, context);
			builder.append(')');
		} else {
			lhs.toString(builder, context);
		}

		context.spacing(builder);
		builder.append(operator.getSymbol());
		context.spacing(builder);

		if ((rhs instanceof BinaryOp)
				&& !((BinaryOp<?>) rhs).getOperator().getPrecedence().higher(super.getOperator().getPrecedence())) {
			builder.append('(');
			rhs.toString(builder, context);
			builder.append(')');
		} else {
			rhs.toString(builder, context);
		}

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if ((rhs instanceof LiteralValue) && (lhs instanceof LiteralValue)) {
			return eval(context);
		} else {
			return this;
		}
	}
}
