package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.derivative.DerivationOperation;

public enum Arithmetic implements Operator<Arithmetic> {
	ADD("+", Precedence.ADD, ValueOperation::add, DerivationOperation::add),
	SUB("-", Precedence.ADD, ValueOperation::sub, DerivationOperation::sub),
	MUL("*", Precedence.MUL, ValueOperation::mul, DerivationOperation::mul),
	DIV("/", Precedence.MUL, ValueOperation::div, DerivationOperation::div),
	MOD("%", Precedence.MUL, ValueOperation::mod, DerivationOperation::nonDerivable),
	NEG("-", Precedence.NEG, DerivationOperation::neg),
	POW("^", Precedence.POW, ValueOperation::pow, DerivationOperation::pow),
	SHL("<<", Precedence.SHIFT, ValueOperation::shl, DerivationOperation::nonDerivable),
	SHR(">>", Precedence.SHIFT, ValueOperation::shr, DerivationOperation::nonDerivable),
	FACT("!", Precedence.FACT, DerivationOperation::nonDerivable);

	private final String symbol;
	private final Precedence precedence;
	private final Operator.Evaluator evaluator;
	private final DerivationOperation.Evaluator derivation;

	private Arithmetic(String symbol, Precedence precedence, Operator.Evaluator evaluator,
			DerivationOperation.Evaluator derivation) {
		this.evaluator = evaluator;
		this.symbol = symbol;
		this.precedence = precedence;
		this.derivation = derivation;
	}

	private Arithmetic(String symbol, Precedence precedence, DerivationOperation.Evaluator derivation) {
		this(symbol, precedence, null, derivation);
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

	public DerivationOperation.Evaluator getDerivation() {
		return derivation;
	}
}
