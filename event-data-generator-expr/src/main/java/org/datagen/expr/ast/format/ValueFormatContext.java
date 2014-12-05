package org.datagen.expr.ast.format;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.Mapped;

public interface ValueFormatContext extends Serializable {

	@Nonnull
	default String formatString(@Nonnull String value) {
		return value;
	}

	@Nonnull
	String formatInteger(long value);

	@Nonnull
	String formatReal(double value);

	@Nonnull
	String formatBoolean(boolean value);

	@Nonnull
	String formatDate(@Nonnull Date value);

	@Nonnull
	String formatLambda(@Nonnull Lambda lambda);

	@Nonnull
	String formatArray(@Nonnull Array array);

	@Nonnull
	String formatMapped(@Nonnull Mapped mapped);
}
