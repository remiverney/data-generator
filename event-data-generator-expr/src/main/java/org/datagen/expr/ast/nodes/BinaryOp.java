package org.datagen.expr.ast.nodes;

import java.util.List;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;

public abstract class BinaryOp<O extends Enum<O> & Operator<O>> extends
		UnaryOp<O> {

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
		return operator.getEvaluator().eval(context, this, lhs.eval(context),
				rhs.eval(context));
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		if ((lhs instanceof BinaryOp)
				&& ((BinaryOp<?>) lhs).getOperator().getPrecedence()
						.less(super.getOperator().getPrecedence())) {
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
				&& !((BinaryOp<?>) rhs).getOperator().getPrecedence()
						.higher(super.getOperator().getPrecedence())) {
			builder.append('(');
			rhs.toString(builder, context);
			builder.append(')');
		} else {
			rhs.toString(builder, context);
		}

		return builder;
	}
}
