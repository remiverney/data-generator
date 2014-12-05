package org.datagen.factory;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.Nonnull;

public interface BuilderParameter<T extends Serializable> {

	@Nonnull
	String getName();

	Class<T> getType();

	Optional<T> getDefaultValue();

	default boolean hasDefaultValue() {
		return getDefaultValue().isPresent();
	}
}
