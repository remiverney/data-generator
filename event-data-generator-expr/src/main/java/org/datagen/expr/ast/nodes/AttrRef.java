package org.datagen.expr.ast.nodes;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.Mapped;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.IncompatibleAttributeException;
import org.datagen.expr.ast.exception.UnknownAttributeException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class AttrRef implements Node {
	private static enum Attribute {
		TYPE,
		LENGTH,
		LAST,
		YEAR,
		MONTH,
		WEEK_YEAR,
		DAY_YEAR,
		DAY_MONTH,
		DAY_WEEK,
		HOUR,
		MINUTE,
		SECOND,
		MILLISECOND,
		TIME,
		ARITY;

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
		Value value = expr.eval(context);

		if (attribute == Attribute.TYPE) {
			return new LiteralValue(value.getType().getTypeName());
		}

		switch (value.getType()) {
		case ARRAY:
			Array array = (Array) value;
			switch (attribute) {
			case LENGTH:
				return new LiteralValue(array.getSize());
			case LAST:
				return array.get(array.getSize() - 1).eval(context);
			default:
				throw new IncompatibleAttributeException(this, value.getType(),
						attribute.name());
			}
		case MAPPED:
			switch (attribute) {
			case LENGTH:
				return new LiteralValue(((Mapped) value).getSize());
			default:
				throw new IncompatibleAttributeException(this, value.getType(),
						attribute.name());
			}
		case DATE_TIME:
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(((LiteralValue) value).getDate());

			switch (attribute) {
			case YEAR:
				return new LiteralValue(calendar.get(Calendar.YEAR));
			case MONTH:
				return new LiteralValue(calendar.get(Calendar.MONTH));
			case WEEK_YEAR:
				return new LiteralValue(calendar.get(Calendar.WEEK_OF_YEAR));
			case DAY_YEAR:
				return new LiteralValue(calendar.get(Calendar.DAY_OF_YEAR));
			case DAY_MONTH:
				return new LiteralValue(calendar.get(Calendar.DAY_OF_MONTH));
			case DAY_WEEK:
				return new LiteralValue(calendar.get(Calendar.DAY_OF_WEEK));
			case HOUR:
				return new LiteralValue(calendar.get(Calendar.HOUR_OF_DAY));
			case MINUTE:
				return new LiteralValue(calendar.get(Calendar.MINUTE));
			case SECOND:
				return new LiteralValue(calendar.get(Calendar.SECOND));
			case MILLISECOND:
				return new LiteralValue(calendar.get(Calendar.MILLISECOND));
			case TIME:
				return new LiteralValue(calendar.getTimeInMillis());
			default:
				throw new IncompatibleAttributeException(this, value.getType(),
						attribute.name());
			}
		case LAMBDA:
			switch (attribute) {
			case ARITY:
				return new LiteralValue(((Lambda) value).getArity());
			default:
				throw new IncompatibleAttributeException(this, value.getType(),
						attribute.name());
			}
		default:
			throw new IncompatibleAttributeException(this, value.getType(),
					attribute.name());
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		expr.toString(builder, context).append('.');

		return builder.append(attribute.identifier());
	}
}
