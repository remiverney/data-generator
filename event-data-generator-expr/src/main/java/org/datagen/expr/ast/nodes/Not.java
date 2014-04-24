package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Not extends UnaryOp<Logic> {

	public Not(Node rhs) {
		super(Logic.NOT, rhs);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.not(context, this, rhs.eval(context));
	}

}
