package org.datagen.expr.ast.derivative;

import org.datagen.expr.ast.intf.Node;

public interface DerivationContext {

	String getVariable();

	Node derive(Node node);
}
