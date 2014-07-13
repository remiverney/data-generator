package org.datagen.expr.ast;

import java.io.Serializable;
import java.util.Date;

public interface ValueFormatContext extends Serializable {

	default String formatString(String value) {
		return value;
	}

	String formatInteger(long value);

	String formatReal(double value);

	String formatBoolean(boolean value);

	String formatDate(Date value);

	String formatLambda(Lambda lambda);

	String formatArray(Array array);

	String formatMapped(Mapped mapped);
}
