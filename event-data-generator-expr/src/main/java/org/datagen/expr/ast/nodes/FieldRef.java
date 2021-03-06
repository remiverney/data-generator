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
public class FieldRef implements Reference {

	private final String field;

	public FieldRef(String field) {
		this.field = field;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = context.getField(field);

		if (value == null) {
			throw new UnresolvedReferenceException(this, field);
		}

		return value;
	}

	@Override
	public String getReference() {
		return field;
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		return builder.append('@').append(field);
	}

	@Override
	public Node optimize(EvalContext context) {
		return Reference.super.optimize(context);
	}

	@Override
	public Node derivative(DerivationContext context) {
		return LiteralValue.ZERO;
	}

}
