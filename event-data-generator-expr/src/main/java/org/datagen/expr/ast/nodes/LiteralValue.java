package org.datagen.expr.ast.nodes;

import java.util.Date;

import org.datagen.expr.ast.derivative.DerivationContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class LiteralValue implements Value {

	public static final LiteralValue TRUE = new LiteralValue(true);
	public static final LiteralValue FALSE = new LiteralValue(false);

	public static final LiteralValue ZERO = new LiteralValue(0);
	public static final LiteralValue ONE = new LiteralValue(1);

	private final ValueType type;

	private final long integer;
	private final double real;
	private final String string;
	private final Date date;
	private final boolean bool;

	public LiteralValue(long integer) {
		this(ValueType.INTEGER, integer, 0.0, null, null, false);
	}

	public LiteralValue(double real) {
		this(ValueType.REAL, 0, real, null, null, false);
	}

	public LiteralValue(String string) {
		this(ValueType.STRING, 0, 0.0, string, null, false);
	}

	public LiteralValue(Date date) {
		this(ValueType.DATE_TIME, 0, 0.0, null, date, false);
	}

	public LiteralValue(boolean bool) {
		this(ValueType.BOOLEAN, 0, 0.0, null, null, bool);
	}

	private LiteralValue(ValueType type, long integer, double real, String string, Date date, boolean bool) {
		this.type = type;
		this.integer = integer;
		this.real = real;
		this.string = string;
		this.date = date;
		this.bool = bool;
	}

	@Override
	public ValueType getType() {
		return type;
	}

	public long getInteger() {
		assert type == ValueType.INTEGER;
		return integer;
	}

	public double getReal() {
		assert type == ValueType.REAL;
		return real;
	}

	public String getString() {
		assert type == ValueType.STRING;
		return string;
	}

	public Date getDate() {
		assert type == ValueType.DATE_TIME;
		return date;
	}

	public boolean getBool() {
		assert type == ValueType.BOOLEAN;
		return bool;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public String toValueString(ValueFormatContext context) {
		switch (type) {
		case BOOLEAN:
			return context.formatBoolean(bool);
		case DATE_TIME:
			return context.formatDate(date);
		case INTEGER:
			return context.formatInteger(integer);
		case REAL:
			return context.formatReal(real);
		case STRING:
			return context.formatString(string);
		default:
			return null;
		}
	}

	public Object get() {
		switch (type) {
		case BOOLEAN:
			return bool;
		case DATE_TIME:
			return date;
		case INTEGER:
			return integer;
		case REAL:
			return real;
		case STRING:
			return string;
		default:
			return null;
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		switch (type) {
		case BOOLEAN:
			return builder.append(bool);
		case DATE_TIME:
			return builder.append(date);
		case INTEGER:
			return builder.append(integer);
		case REAL:
			return builder.append(real);
		case STRING:
			return builder.append('"').append(string.replace("\"", "\\\"")).append('"');
		default:
			return builder;
		}
	}

	@Override
	public String toString() {
		return type + ": " + get();
	}

	public static LiteralValue from(boolean bool) {
		return bool ? TRUE : FALSE;
	}

	@Override
	public int hashCode() {
		switch (type) {
		case BOOLEAN:
			return Boolean.hashCode(bool);
		case DATE_TIME:
			return date.hashCode();
		case INTEGER:
			return Long.hashCode(integer);
		case REAL:
			return Double.hashCode(real);
		case STRING:
			return string.hashCode();
		default:
			throw new IllegalArgumentException("Unexpected literal value type [ " + type + " ]");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		LiteralValue other = (LiteralValue) obj;

		if (type != other.getType()) {
			return false;
		}

		switch (type) {
		case BOOLEAN:
			return bool == other.getBool();
		case DATE_TIME:
			return date.equals(other.getDate());
		case INTEGER:
			return integer == other.getInteger();
		case REAL:
			return real == other.getReal();
		case STRING:
			return string.equals(other.getString());
		default:
			throw new IllegalArgumentException("Unexpected literal value type [ " + type + " ]");
		}
	}

	@Override
	public Node derivative(DerivationContext context) {
		return ZERO;
	}
}
