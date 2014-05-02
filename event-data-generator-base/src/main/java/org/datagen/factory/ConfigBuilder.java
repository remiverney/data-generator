package org.datagen.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConfigBuilder<P extends BuilderParameter<?>> implements Config<P>,
		GenericBuilder<P, Config<P>, ConfigBuilder<P>> {

	private final Map<P, Object> parameters = new HashMap<>();

	public ConfigBuilder() {
	}

	@Override
	public Config<P> build() {
		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> set(P parameter, C value) {
		parameters.put(parameter, value);

		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> unset(P parameter) {
		parameters.remove(parameter);

		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> enable(P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return this.set(parameter, true);
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> disable(P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return this.set(parameter, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Serializable> C get(P parameter) {
		return (parameters.containsKey(parameter)) ? (C) parameters
				.get(parameter) : (C) parameter.getDefaultValue();
	}

	@Override
	public boolean isEnabled(P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return get(parameter);
	}

}
