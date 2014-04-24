package org.datagen.expr.ast.intf;

import java.util.List;

public interface Composite<T extends Node> {

	List<T> getChildren();
}
