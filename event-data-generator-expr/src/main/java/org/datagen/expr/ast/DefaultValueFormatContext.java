package org.datagen.expr.ast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultValueFormatContext extends SimpleValueFormatContext {

	private static final Locale DEFAULT_LOCALE = Locale.US;

	private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat
			.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
					DEFAULT_LOCALE);

	private static final NumberFormat DEFAULT_NUMBER_FORMAT = NumberFormat
			.getNumberInstance(DEFAULT_LOCALE);

	private final DateFormat dateFormat;
	private final NumberFormat numberFormat;

	public DefaultValueFormatContext() {
		this(DEFAULT_DATE_FORMAT, DEFAULT_NUMBER_FORMAT);
	}

	public DefaultValueFormatContext(DateFormat dateFormat,
			NumberFormat numberFormat) {
		this.dateFormat = dateFormat;
		this.numberFormat = numberFormat;
	}

	@Override
	public String formatInteger(long value) {
		return this.numberFormat.format(value);
	}

	@Override
	public String formatReal(double value) {
		return this.numberFormat.format(value);
	}

	@Override
	public String formatBoolean(boolean value) {
		return String.valueOf(value);
	}

	@Override
	public String formatDate(Date value) {
		return this.dateFormat.format(value);
	}
}
