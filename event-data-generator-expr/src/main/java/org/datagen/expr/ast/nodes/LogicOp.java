package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;

public class LogicOp extends BinaryOp<Logic> {

	public LogicOp(Node lhs, Node rhs, Logic operator) {
		super(lhs, rhs, operator);
	}

}
