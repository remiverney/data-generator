package org.datagen.config;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;

public interface Configurable<P extends BuilderParameter<?>> {

	@Nonnull
	Optional<Config<P>> getConfiguration();
}
