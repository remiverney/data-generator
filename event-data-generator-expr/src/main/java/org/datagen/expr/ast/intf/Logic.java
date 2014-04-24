package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Logic implements Operator<Logic> {
	OR(ValueOperation::or), XOR(ValueOperation::xor), AND(ValueOperation::and), NOT(
			null);

	private final Operator.Evaluator evaluator;

	private Logic(Operator.Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public Operator.Evaluator getEvaluator() {
		return evaluator;
	}
}
