package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.intf.Reference;
import org.datagen.expr.ast.intf.Value;

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
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		return builder.append(variable);
	}
}
