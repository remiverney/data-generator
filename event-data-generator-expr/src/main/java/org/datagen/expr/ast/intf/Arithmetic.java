package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;

public enum Arithmetic implements Operator<Arithmetic> {
	ADD(ValueOperation::add, "+", Precedence.ADD),
	SUB(ValueOperation::sub, "-", Precedence.ADD),
	MUL(ValueOperation::mul, "*", Precedence.MUL),
	DIV(ValueOperation::div, "/", Precedence.MUL),
	MOD(ValueOperation::mod, "%", Precedence.MUL),
	NEG(null, "-", Precedence.NEG),
	POW(ValueOperation::pow, "^", Precedence.POW),
	FACT(null, "!", Precedence.FACT);

	private final Operator.Evaluator evaluator;
	private final String symbol;
	private final Precedence precedence;

	private Arithmetic(Operator.Evaluator evaluator, String symbol,
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
