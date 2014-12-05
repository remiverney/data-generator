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
public class Negation extends UnaryOp<Arithmetic> {

	public Negation(Node rhs) {
		super(Arithmetic.NEG, rhs);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.neg(context, this, rhs.eval(context));
	}

	@Override
	public Node optimize(EvalContext context) {
		if (rhs instanceof LiteralValue) {
			switch (((LiteralValue) rhs).getType()) {
			case INTEGER:
				return new LiteralValue(-((LiteralValue) rhs).getInteger());
			case REAL:
				return new LiteralValue(-((LiteralValue) rhs).getReal());
			default:
				break;
			}
		} else if (rhs instanceof Negation) {
			if ((((LiteralValue) rhs).getType() == ValueType.INTEGER)
					|| (((LiteralValue) rhs).getType() == ValueType.REAL)) {
				return ((Negation) rhs).getRhs();
			}
		}

		return this;
	}

	@Override
	public void validate(ValidationContext context) {
		if (rhs instanceof Value) {
			if ((((Value) rhs).getType() != ValueType.INTEGER) && ((Value) rhs).getType() != ValueType.INTEGER) {
				context.addStatus(StatusLevel.ERROR, new IncompatibleTypesException(this, Arithmetic.NEG, (Value) rhs));
			}
		} else {
			super.validate(context);
		}
	}
}
