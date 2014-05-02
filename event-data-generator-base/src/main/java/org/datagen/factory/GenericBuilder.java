package org.datagen.factory;

import java.io.Serializable;

public interface GenericBuilder<P extends BuilderParameter<?>, T, B extends GenericBuilder<P, T, B>> {

	<C extends Serializable> B set(P parameter, C value);

	<C extends Serializable> B unset(P parameter);

	<C extends Serializable> B enable(P parameter);

	<C extends Serializable> B disable(P parameter);

	T build();
}
