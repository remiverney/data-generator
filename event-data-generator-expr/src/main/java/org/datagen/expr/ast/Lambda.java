package org.datagen.expr.ast;

import java.util.Collection;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface Lambda {

	int getArity();

	Node getBody();

	Node getDerivative(String variable);

	Value eval(EvalContext context, Value... parameters);

	Value eval(EvalContext context, Collection<Value> parameters);
}
