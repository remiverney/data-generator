package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;

public abstract class UnaryOp<O extends Operator<O>> implements Node {

	protected final O operator;
	protected final Node rhs;

	protected UnaryOp(O operator, Node rhs) {
		this.operator = operator;
		this.rhs = rhs;
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
}
