package org.datagen.expr;

import java.util.Date;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface DateProvider {

	@Nonnull
	Date getDate();
}
