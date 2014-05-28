package org.datagen.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerUmpl<T extends Closeable> implements EntityManager<T> {

	private final Map<String, T> entities = new HashMap<>();

	public EntityManagerUmpl() {
	}

	@Override
	public void close() throws IOException {
		for (T entity : this.entities.values()) {
			entity.close();
		}

		this.entities.clear();
	}

	@Override
	public Map<String, T> getAll() {
		return this.entities;
	}

	@Override
	public T get(String name) {
		return this.entities.get(name);
	}

}
