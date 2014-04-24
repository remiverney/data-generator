package org.datagen.expr.ast.nodes;

import java.util.List;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;

public abstract class BinaryOp<O extends Enum<O> & Operator<O>> extends UnaryOp<O> {

	protected final Node lhs;

	public BinaryOp(Node lhs, Node rhs, O operator) {
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
}
