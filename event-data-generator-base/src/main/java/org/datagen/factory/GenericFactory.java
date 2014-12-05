package org.datagen.factory;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.datagen.utils.Identifiable;

public interface GenericFactory<I extends Serializable & Comparable<I>, T extends Identifiable<I>, P extends BuilderParameter<?>> {

	default @Nonnull T get() {
		return get(Optional.<Config<P>> empty());
	}

	@Nonnull
	T get(Optional<Config<P>> config);

	@Nonnull
	T get(I name, Optional<Config<P>> config);

	@Nonnull
	ConfigBuilder<P> getConfigBuilder();
}
