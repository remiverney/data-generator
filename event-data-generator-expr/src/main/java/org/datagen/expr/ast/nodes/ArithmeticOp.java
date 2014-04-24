package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;

public class ArithmeticOp extends BinaryOp<Arithmetic> {

	public ArithmeticOp(Node lhs, Node rhs, Arithmetic operator) {
		super(lhs, rhs, operator);
	}

}
