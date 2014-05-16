package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.context.EvalContext;

public interface Operator<T> {

	@FunctionalInterface
	public interface Evaluator {
		Value eval(EvalContext context, Node node, Value lhs, Value rhs);
	}

	Evaluator getEvaluator();

	String getSymbol();

	Precedence getPrecedence();
}
