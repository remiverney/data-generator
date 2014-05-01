package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Factorial extends UnaryOp<Arithmetic> {

	public Factorial(Node rhs) {
		super(Arithmetic.FACT, rhs);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.fact(context, this, rhs.eval(context));
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		rhs.toString(builder, context);
		builder.append(operator.getSymbol());

		return builder;
	}

}
