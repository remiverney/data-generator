package org.datagen.expr.interpreter;

import java.util.Optional;
import java.util.UUID;

import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;
import org.datagen.factory.GenericFactory;
import org.datagen.utils.annotation.Immutable;

@Immutable
public final class InterpreterFactory implements GenericFactory<String, Interpreter, InterpreterParameters> {

	private static final InterpreterFactory INSTANCE = new InterpreterFactory();

	private InterpreterFactory() {
	}

	public static InterpreterFactory instance() {
		return INSTANCE;
	}

	@Override
	public Interpreter get() {
		return new InterpreterImpl(buildName());
	}

	@Override
	public Interpreter get(Optional<Config<InterpreterParameters>> config) {
		return new InterpreterImpl(buildName(), config);
	}

	@Override
	public ConfigBuilder<InterpreterParameters> getConfigBuilder() {
		return new ConfigBuilder<InterpreterParameters>();
	}

	@Override
	public Interpreter get(String name, Optional<Config<InterpreterParameters>> config) {
		return new InterpreterImpl(name, config);
	}

	private static String buildName() {
		return UUID.randomUUID().toString();
	}

}
