package org.datagen.expr.ast;

import java.util.Set;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.intf.Node;

public interface Library {

	@Nonnull
	String getName();

	Node get(@Nonnull String entry);

	@Nonnull
	Set<String> getEntries();
}
