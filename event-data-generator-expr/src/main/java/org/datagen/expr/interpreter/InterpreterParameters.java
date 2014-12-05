package org.datagen.expr.interpreter;

import java.io.Serializable;
import java.util.Optional;

import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.factory.BuilderParameter;

public enum InterpreterParameters implements BuilderParameter<Serializable> {

	ALLOW_LAMBDA_DEFINITION(boolean.class, true),
	ALLOW_COMPARISON(boolean.class, true),
	ALLOW_LOGIC(boolean.class, true),
	ALLOW_ARITHMETIC(boolean.class, true),
	ALLOW_FUNCTION(boolean.class, true),
	ALLOW_LIBRARY_REFERENCE(boolean.class, true),
	ALLOW_JAVA_REFERENCE(boolean.class, true),
	ALLOW_ARRAY(boolean.class, true),
	ALLOW_MAPPED(boolean.class, true),
	ALLOW_PROPERTY_REFERENCE(boolean.class, true),
	ALLOW_FIELD_REFERENCE(boolean.class, true),

	ENABLE_PARALLEL(boolean.class, true),
	ENABLE_OPTIMIZATIONS(boolean.class, true),

	OPTIMIZER_FORMATTER(ValueFormatContext.class);

	private final Class<? extends Serializable> type;
	private final Optional<Serializable> defaultValue;

	private <T extends Serializable> InterpreterParameters(Class<T> type) {
		this.type = type;
		this.defaultValue = Optional.empty();
	}

	private <T extends Serializable> InterpreterParameters(Class<T> type, T defaultValue) {
		this.type = type;
		this.defaultValue = Optional.of(defaultValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Serializable> getType() {
		return (Class<Serializable>) this.type;
	}

	@Override
	public Optional<Serializable> getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public String getName() {
		return name();
	}

}
