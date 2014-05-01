package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Comparison implements Operator<Comparison> {
	EQUAL(ValueOperation::equal, "=", Precedence.COMP),
	NOT_EQUAL(ValueOperation::notEqual, "!=", Precedence.COMP),
	LESS(ValueOperation::less, "<", Precedence.COMP),
	LESS_EQUAL(ValueOperation::lessEqual, "<=", Precedence.COMP),
	GREATER(ValueOperation::greater, ">", Precedence.COMP),
	GREATER_EQUAL(ValueOperation::greaterEqual, ">=", Precedence.COMP);

	private final Operator.Evaluator evaluator;
	private final String symbol;
	private final Precedence precedence;

	private Comparison(Operator.Evaluator evaluator, String symbol,
			Precedence precedence) {
		this.evaluator = evaluator;
		this.symbol = symbol;
		this.precedence = precedence;
	}

	@Override
	public Operator.Evaluator getEvaluator() {
		return evaluator;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	@Override
	public Precedence getPrecedence() {
		return precedence;
	}
}
