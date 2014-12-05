package org.datagen.expr.ast;

import java.util.List;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.intf.Node;

public interface Array extends Iterable<Node> {

	int getSize();

	@Nonnull
	Node get(long index);

	@Nonnull
	List<Node> getRange(long start, long end);

	@Nonnull
	List<? extends Node> getAll();
}
