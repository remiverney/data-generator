package org.datagen.expr.ast.functions;

import java.util.List;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface FunctionRegistry {

	Object getFunction(String name);

	boolean isDeterministic(Node node, String name);

	Value invokeFunction(Node node, String name, List<Value> parameters);

}