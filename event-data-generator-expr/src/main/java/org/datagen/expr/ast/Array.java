package org.datagen.expr.ast;

import java.util.List;

import org.datagen.expr.ast.intf.Node;

public interface Array {

	String getName();

	int getSize();

	Node get(long index);

	List<Node> getRange(long start, long end);

	List<Node> getAll();
}
