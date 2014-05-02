package org.datagen.factory;

public interface GenericFactory<T, P extends BuilderParameter<?>> {

	default T get() {
		return get(null);
	}

	T get(Config<P> config);

	ConfigBuilder<P> getConfigBuilder();
}
