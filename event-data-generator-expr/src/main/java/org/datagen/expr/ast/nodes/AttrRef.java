package org.datagen.expr.ast.nodes;

import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.exception.IncompatibleAttributeException;
import org.datagen.expr.ast.exception.UnknownAttributeException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class AttrRef implements Node {
	private static enum Attribute {
		LENGTH;

		private String identifier() {
			return name().toLowerCase();
		}
	}

	private final Node expr;
	private final Attribute attribute;

	public AttrRef(Node expr, String attribute) {
		this.expr = expr;

		try {
			this.attribute = Attribute.valueOf(attribute.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new UnknownAttributeException(this, attribute);
		}
	}

	@Override
	public List<Node> getChildren() {
		return Collections.singletonList(expr);
	}

	@Override
	public Value eval(EvalContext context) {
		switch (attribute) {
		case LENGTH:
			Value value = expr.eval(context);
			if (!value.isArray()) {
				throw new IncompatibleAttributeException(this, value.getType(),
						attribute.name());
			}
			return new LiteralValue(((Array) value).getSize());
		default:
			throw new IllegalArgumentException(
					"Unexpected expression attribute '" + attribute + "'");
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		expr.toString(builder, context).append('.');

		return builder.append(attribute.identifier());
	}
}
