package org.datagen.expr;

import java.util.Date;

@FunctionalInterface
public interface DateProvider {

	Date getDate();
}
