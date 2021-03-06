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
public class PropertyRef implements Reference {

	private final String property;

	public PropertyRef(String property) {
		this.property = property;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = context.getProperty(property);

		if (value == null) {
			throw new UnresolvedReferenceException(this, property);
		}

		return value;
	}

	@Override
	public String getReference() {
		return property;
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		return builder.append('$').append(property);
	}

	@Override
	public Node derivative(DerivationContext context) {
		return LiteralValue.ZERO;
	}

}
