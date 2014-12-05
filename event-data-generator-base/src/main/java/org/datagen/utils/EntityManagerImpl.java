package org.datagen.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerImpl<I extends Comparable<I>, T extends Closeable> implements EntityManager<I, T> {

	private final Map<I, T> entities = new HashMap<>();

	public EntityManagerImpl() {
	}

	@Override
	public void close() throws IOException {
		for (T entity : this.entities.values()) {
			entity.close();
		}

		this.entities.clear();
	}

	@Override
	public Map<I, T> getAll() {
		return this.entities;
	}

	@Override
	public T get(I name) {
		return this.entities.get(name);
	}

	@Override
	public void register(I name, T entity) {
		this.entities.put(name, entity);
	}

	@Override
	public void unregister(I name) {
		this.entities.remove(name);
	}

}
