package org.datagen.expr.ast.intf;

import javax.annotation.Nonnull;

public interface Reference extends Node {

	@Nonnull
	String getReference();
}
