package org.datagen.expr.ast;

import java.util.List;

import org.datagen.expr.ast.intf.Node;

public interface Array extends Iterable<Node> {

	String getName();

	int getSize();

	Node get(long index);

	List<Node> getRange(long start, long end);

	List<? extends Node> getAll();
}
