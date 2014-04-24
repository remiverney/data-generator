package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Comparison implements Operator<Comparison> {
	EQUAL(ValueOperation::equal), NOT_EQUAL(ValueOperation::notEqual), LESS(
			ValueOperation::less), LESS_EQUAL(ValueOperation::lessEqual), GREATER(
			ValueOperation::greater), GREATER_EQUAL(
			ValueOperation::greaterEqual);

	private final Operator.Evaluator evaluator;

	private Comparison(Operator.Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public Operator.Evaluator getEvaluator() {
		return evaluator;
	}
}
