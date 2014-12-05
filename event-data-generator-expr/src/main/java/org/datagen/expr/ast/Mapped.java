package org.datagen.expr.ast;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface Mapped extends Iterable<Node> {

	int getSize();

	@Nonnull
	Node get(@Nonnull Value key);

	@Nonnull
	List<? extends Node> getKeys();

	@Nonnull
	List<? extends Node> getValues();

	@Nonnull
	Map<Node, Node> getAll();
}
