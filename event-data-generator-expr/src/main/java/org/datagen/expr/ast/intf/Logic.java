package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Logic implements Operator<Logic> {
	OR(ValueOperation::or, "OR", Precedence.OR),
	XOR(ValueOperation::xor, "XOR", Precedence.OR),
	AND(ValueOperation::and, "AND", Precedence.AND),
	NOT("NOT", Precedence.NOT);

	private final Operator.Evaluator evaluator;
	private final String symbol;
	private final Precedence precedence;

	private Logic(String symbol, Precedence precedence) {
		this(null, symbol, precedence);
	}

	private Logic(Operator.Evaluator evaluator, String symbol, Precedence precedence) {
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
