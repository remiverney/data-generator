package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationResult.StatusLevel;
import org.datagen.expr.ast.exception.IncompatibleTypesException;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
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
				return eval(context);
			}
		}

		return this;
	}

	@Override
	public void validate(ValidationContext context) {
		if (rhs instanceof Value) {
			if (((LiteralValue) rhs).getType() != ValueType.INTEGER) {
				context.addStatus(StatusLevel.ERROR, new IncompatibleTypesException(this, Arithmetic.FACT, (Value) rhs));
			}
		} else {
			super.validate(context);
		}
	}

}
