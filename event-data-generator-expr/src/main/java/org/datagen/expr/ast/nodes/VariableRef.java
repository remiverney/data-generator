package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.derivative.DerivationContext;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Reference;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class VariableRef implements Reference {

	private final String variable;

	public VariableRef(String variable) {
		this.variable = variable;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = context.getVariable(variable);

		if (value == null) {
			throw new UnresolvedReferenceException(this, variable);
		}

		return value;
	}

	@Override
	public String getReference() {
		return variable;
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		return builder.append(variable);
	}

	@Override
	public Node derivative(DerivationContext context) {
		if (this.variable.equals(context.getVariable())) {
			return LiteralValue.ONE;
		} else {
			return LiteralValue.ZERO;
		}
	}
}
