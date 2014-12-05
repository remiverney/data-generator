package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Reference;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class ThisRef implements Reference {

	public static final String THIS_VARIABLE = "#this#";
	public static final String THIS = "this";

	public ThisRef() {
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = context.getVariable(THIS_VARIABLE);

		if (value == null) {
			throw new UnresolvedReferenceException(this, "this");
		}

		return value;
	}

	@Override
	public String getReference() {
		return THIS;
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		return builder.append(THIS);
	}

}
