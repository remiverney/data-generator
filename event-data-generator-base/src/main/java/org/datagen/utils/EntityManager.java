package org.datagen.utils;

import java.io.Closeable;

public interface EntityManager<I extends Comparable<I>, T extends Closeable> extends EntityRegistry<I, T> {

	void register(I name, T entity);

	void unregister(I name);
}
