package org.datagen.expr.ast.intf;

@FunctionalInterface
public interface Visitable<T> {

	@FunctionalInterface
	interface Visitor<T> {
		T visit(T visited);
	}

	T visit(Visitor<T> visitor);
}
