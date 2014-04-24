package org.datagen.expr.ast.intf;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ValueFormatContext;

public interface Value extends Node {

	default @Override public Value eval(EvalContext context) {
		return this;
	}

	default public boolean isArray() {
		return false;
	}

	default public boolean isLambda() {
		return false;
	}

	default public boolean isField() {
		return false;
	}

	default public boolean isProperty() {
		return false;
	}

	default public boolean isVariable() {
		return false;
	}

	default public boolean isLiteral() {
		return false;
	}

	ValueType getType();

	String toValueString(ValueFormatContext context);
}
