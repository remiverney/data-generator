package org.datagen.expr.ast;

import java.util.Set;

import org.datagen.expr.ast.intf.Node;

public interface Library {

	String getName();

	Node get(String entry);

	Set<String> getEntries();
}
