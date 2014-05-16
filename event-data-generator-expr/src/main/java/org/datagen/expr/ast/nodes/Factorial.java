package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Factorial extends UnaryOp<Arithmetic> {

	public Factorial(Node rhs) {
		super(Arithmetic.FACT, rhs, false);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.fact(context, this, rhs.eval(context));
	}

}
