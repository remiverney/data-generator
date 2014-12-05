package org.datagen.utils;

import java.io.Closeable;
import java.util.Map;

public interface EntityRegistry<I extends Comparable<I>, T extends Closeable> extends Closeable {

	Map<I, T> getAll();

	T get(I name);
}
