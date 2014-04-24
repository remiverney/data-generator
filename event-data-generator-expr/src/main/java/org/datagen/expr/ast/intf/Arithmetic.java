package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Arithmetic implements Operator<Arithmetic> {
	ADD(ValueOperation::add), SUB(ValueOperation::sub), MUL(ValueOperation::mul), DIV(
			ValueOperation::div), MOD(ValueOperation::mod), NEG(null);

	private final Operator.Evaluator evaluator;

	private Arithmetic(Operator.Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public Operator.Evaluator getEvaluator() {
		return evaluator;
	}
}
