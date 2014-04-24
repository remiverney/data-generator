package org.datagen.expr.ast;

import java.util.Date;

public interface ValueFormatContext {

	String formatInteger(long value);

	String formatReal(double value);

	String formatBoolean(boolean value);

	String formatDate(Date value);

	String formatLambda(Lambda lambda);

	String formatArray(Array array);
}
