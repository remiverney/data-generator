package org.datagen.factory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.w3c.dom.Element;

public interface GenericBuilder<P extends BuilderParameter<?>, T, B extends GenericBuilder<P, T, B>> {

	@Nonnull
	<C extends Serializable> B set(@Nonnull P parameter, Optional<C> value);

	@Nonnull
	<C extends Serializable> B set(@Nonnull P parameter, C value);

	@Nonnull
	<C extends Serializable> B unset(@Nonnull P parameter);

	@Nonnull
	<C extends Serializable> B enable(@Nonnull P parameter);

	@Nonnull
	<C extends Serializable> B disable(@Nonnull P parameter);

	@Nonnull
	<C extends Serializable> B setXml(@Nonnull Collection<Element> elements, @Nonnull Class<P> clazz);

	@Nonnull
	T build();
}
