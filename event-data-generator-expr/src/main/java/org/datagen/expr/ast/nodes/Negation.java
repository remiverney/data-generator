package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Negation extends UnaryOp<Arithmetic> {

	public Negation(Node rhs) {
		super(Arithmetic.NEG, rhs);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.neg(context, this, rhs.eval(context));
	}

}
