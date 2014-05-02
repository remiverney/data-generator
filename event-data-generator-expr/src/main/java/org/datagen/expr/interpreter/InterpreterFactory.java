package org.datagen.expr.interpreter;

import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;
import org.datagen.factory.GenericFactory;

public final class InterpreterFactory implements
		GenericFactory<Interpreter, InterpreterParameters> {

	private static final InterpreterFactory INSTANCE = new InterpreterFactory();

	private InterpreterFactory() {
	}

	public static InterpreterFactory instance() {
		return INSTANCE;
	}

	@Override
	public Interpreter get() {
		return new InterpreterImpl();
	}

	@Override
	public Interpreter get(Config<InterpreterParameters> config) {
		Interpreter inter = new InterpreterImpl();
		inter.setConfiguration(config);

		return inter;
	}

	@Override
	public ConfigBuilder<InterpreterParameters> getConfigBuilder() {
		return new ConfigBuilder<InterpreterParameters>();
	}

}
