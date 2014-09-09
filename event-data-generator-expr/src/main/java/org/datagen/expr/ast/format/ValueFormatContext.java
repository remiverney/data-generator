package org.datagen.expr.ast.format;

import java.io.Serializable;
import java.util.Date;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.Mapped;

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
