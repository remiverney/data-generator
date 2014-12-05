package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationResult.StatusLevel;
import org.datagen.expr.ast.exception.IncompatibleTypesException;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class LogicOp extends BinaryOp<Logic> {

	public LogicOp(Node lhs, Node rhs, Logic operator) {
		super(lhs, rhs, operator);
	}

	@Override
	public void validate(ValidationContext context) {
		if (lhs instanceof Value) {
			if (((Value) lhs).getType() != ValueType.BOOLEAN) {
				context.addStatus(StatusLevel.ERROR, new IncompatibleTypesException(this, operator, (Value) lhs));
			}
		}

		if (rhs instanceof Value) {
			if (((Value) rhs).getType() != ValueType.BOOLEAN) {
				context.addStatus(StatusLevel.ERROR, new IncompatibleTypesException(this, operator, (Value) rhs));
			}
		}

		super.validate(context);
	}

	@Override
	public Node optimize(EvalContext context) {
		if ((lhs instanceof Value)
				&& (((Value) lhs).getType() == ValueType.BOOLEAN && (rhs instanceof Value) && (((Value) rhs).getType() == ValueType.BOOLEAN))) {
			return super.optimize(context);
		} else {
			return this;
		}
	}
}
