package org.datagen.expr.ast.intf;

public enum Precedence {
	NONE,
	FACT,
	NEG,
	POW,
	MUL,
	ADD,
	COMP,
	NOT,
	AND,
	OR;

	public boolean higher(Precedence other) {
		return this.ordinal() < other.ordinal();
	}

	public boolean less(Precedence other) {
		return this.ordinal() > other.ordinal();
	}

}
