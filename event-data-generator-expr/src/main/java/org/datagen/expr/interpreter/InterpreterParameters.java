package org.datagen.expr.interpreter;

import java.io.Serializable;

import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.factory.BuilderParameter;

public enum InterpreterParameters implements BuilderParameter<Serializable> {

	ALLOW_LAMBDA_DEFINITION(boolean.class, true),
	ALLOW_COMPARISON(boolean.class, true),
	ALLOW_LOGIC(boolean.class, true),
	ALLOW_ARITHMETIC(boolean.class, true),
	ALLOW_FUNCTION(boolean.class, true),
	ALLOW_LIBRARY_REFERENCE(boolean.class, true),
	ALLOW_ARRAY(boolean.class, true),
	ALLOW_MAPPED(boolean.class, true),
	ALLOW_PROPERTY_REFERENCE(boolean.class, true),
	ALLOW_FIELD_REFERENCE(boolean.class, true),

	ENABLE_PARALLEL(boolean.class, true),
	ENABLE_OPTIMIZATIONS(boolean.class, true),
	OPTIMIZER_FORMATTER(ValueFormatContext.class, null);

	private final Class<? extends Serializable> type;
	private final Serializable defaultValue;

	private <T extends Serializable> InterpreterParameters(Class<T> type, T defaultValue) {
		this.type = type;
		this.defaultValue = defaultValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Serializable> getType() {
		return (Class<Serializable>) this.type;
	}

	@Override
	public Serializable getDefaultValue() {
		return this.defaultValue;
	}

}
