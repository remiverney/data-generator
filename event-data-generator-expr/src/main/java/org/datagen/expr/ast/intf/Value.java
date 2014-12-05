package org.datagen.expr.ast.intf;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ValueFormatContext;

public interface Value extends Node {

	default @Override public Value eval(EvalContext context) {
		return this;
	}

	default public boolean isMapped() {
		return false;
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

	@Nonnull
	ValueType getType();

	@Nonnull
	String toValueString(ValueFormatContext context);
}
