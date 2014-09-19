package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class Factorial extends UnaryOp<Arithmetic> {

	public Factorial(Node rhs) {
		super(Arithmetic.FACT, rhs, false);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.fact(context, this, rhs.eval(context));
	}

	@Override
	public Node optimize(EvalContext context) {
		if (rhs instanceof LiteralValue) {
			if (((LiteralValue) rhs).getType() == ValueType.INTEGER) {
				return eval(null);
			}
		}

		return super.optimize(context);
	}

}
