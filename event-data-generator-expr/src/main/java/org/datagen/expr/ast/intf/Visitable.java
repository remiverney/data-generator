package org.datagen.expr.ast.intf;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Visitable<T> {

	@FunctionalInterface
	interface Visitor<T> {
		T visit(@Nonnull T visited);
	}

	T visit(@Nonnull Visitor<T> visitor);
}
