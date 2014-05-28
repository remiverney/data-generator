package org.datagen.utils;

import java.io.Closeable;
import java.util.Map;

public interface EntityManager<T extends Closeable> extends Closeable {

	Map<String, T> getAll();

	T get(String name);
}
