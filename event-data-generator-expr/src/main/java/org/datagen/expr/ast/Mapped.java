package org.datagen.expr.ast;

import java.util.List;
import java.util.Map;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface Mapped extends Iterable<Node> {

	String getName();

	int getSize();

	Node get(Value key);

	List<? extends Node> getKeys();

	List<? extends Node> getValues();

	Map<Node, Node> getAll();
}
