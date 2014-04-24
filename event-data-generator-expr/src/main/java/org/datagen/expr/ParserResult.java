package org.datagen.expr;

import java.util.Collection;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.nodes.FieldRef;

public interface ParserResult {

	Node getRoot();

	Collection<FieldRef> getReferences();
}
