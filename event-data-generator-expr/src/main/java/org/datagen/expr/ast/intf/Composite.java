package org.datagen.expr.ast.intf;

import java.util.List;

import javax.annotation.Nonnull;

public interface Composite<T extends Node> {

	@Nonnull
	List<T> getChildren();
}
