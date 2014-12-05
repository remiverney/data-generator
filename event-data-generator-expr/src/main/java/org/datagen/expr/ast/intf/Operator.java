package org.datagen.expr.ast.intf;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;

public interface Operator<T> {

	@FunctionalInterface
	public interface Evaluator {
		@Nonnull
		Value eval(EvalContext context, Node node, Value lhs, Value rhs);
	}

	@Nonnull
	Evaluator getEvaluator();

	@Nonnull
	String getSymbol();

	@Nonnull
	Precedence getPrecedence();
}
