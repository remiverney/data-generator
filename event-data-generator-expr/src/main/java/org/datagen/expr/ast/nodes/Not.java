package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class Not extends UnaryOp<Logic> {

	public Not(Node rhs) {
		super(Logic.NOT, rhs);
	}

	@Override
	public Value eval(EvalContext context) {
		return ValueOperation.not(context, this, rhs.eval(context));
	}

	@Override
	public Node optimize(EvalContext context) {
		if (rhs instanceof LiteralValue) {
			if (((LiteralValue) rhs).getType() == ValueType.BOOLEAN) {
				return LiteralValue.from(!((LiteralValue) rhs).getBool());
			}
		}

		return this;
	}

}
