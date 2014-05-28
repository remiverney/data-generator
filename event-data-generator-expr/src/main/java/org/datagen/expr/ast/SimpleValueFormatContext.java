package org.datagen.expr.ast;

import java.util.Date;
import java.util.stream.Collectors;

public class SimpleValueFormatContext implements ValueFormatContext {

	private static final long serialVersionUID = 1L;

	public SimpleValueFormatContext() {
	}

	@Override
	public String formatInteger(long value) {
		return String.valueOf(value);
	}

	@Override
	public String formatReal(double value) {
		return String.valueOf(value);
	}

	@Override
	public String formatBoolean(boolean value) {
		return String.valueOf(value);
	}

	@Override
	public String formatDate(Date value) {
		return value.toString();
	}

	@Override
	public String formatLambda(Lambda lambda) {
		throw new UnsupportedOperationException(
				"Cannot format a lambda function");
	}

	@Override
	public String formatArray(Array array) {
		return array.getAll().stream().sequential()
				.map(x -> x.eval(null).toValueString(this))
				.collect(Collectors.joining(", ", "{ ", " }"));
	}

}
