package org.datagen.factory;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

public interface Config<P extends BuilderParameter<?>> {

	<C extends Serializable> Optional<C> get(@Nonnull P parameter);

	Set<P> getKeys();

	boolean isEnabled(P parameter);

	static <P extends BuilderParameter<?>> Config<P> ensure(Optional<Config<P>> configuration) {
		return configuration.orElseGet(() -> new ConfigBuilder<P>().build());
	}
}
