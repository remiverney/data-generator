package org.datagen.expr.ast.nodes;

import java.util.Date;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.ValueFormatContext;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class LiteralValue implements Value {

	public static final LiteralValue TRUE = new LiteralValue(true);
	public static final LiteralValue FALSE = new LiteralValue(false);

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

	private LiteralValue(ValueType type, long integer, double real,
			String string, Date date, boolean bool) {
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
		return integer;
	}

	public double getReal() {
		return real;
	}

	public String getString() {
		return string;
	}

	public Date getDate() {
		return date;
	}

	public boolean getBool() {
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
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
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
			return builder.append('"').append(string).append('"');
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
}
