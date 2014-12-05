package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.derivative.DerivationContext;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class ArithmeticOp extends BinaryOp<Arithmetic> {

	public ArithmeticOp(Node lhs, Node rhs, Arithmetic operator) {
		super(lhs, rhs, operator);
	}

	@Override
	public Node derivative(DerivationContext context) {
		return super.getOperator().getDerivation().derivative(context, this);
	}

}
