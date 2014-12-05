package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.intf.Comparison;
import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class ComparisonOp extends BinaryOp<Comparison> {

	public ComparisonOp(Node lhs, Node rhs, Comparison operator) {
		super(lhs, rhs, operator);
	}

}
