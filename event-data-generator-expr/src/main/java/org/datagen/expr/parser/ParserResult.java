package org.datagen.expr.parser;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.nodes.FieldRef;

public interface ParserResult {

	@Nonnull
	Node getRoot();

	@Nonnull
	Collection<FieldRef> getReferences();
}
