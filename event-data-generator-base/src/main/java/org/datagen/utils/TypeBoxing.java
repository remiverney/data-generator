package org.datagen.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class TypeBoxing {

	private static final Map<Class<?>, Function<String, Serializable>> BOXING = new HashMap<>();

	static {
		BOXING.put(boolean.class, Boolean::valueOf);
		BOXING.put(byte.class, Byte::valueOf);
		BOXING.put(char.class, x -> x.charAt(0));
		BOXING.put(float.class, Float::valueOf);
		BOXING.put(int.class, Integer::valueOf);
		BOXING.put(long.class, Long::valueOf);
		BOXING.put(short.class, Short::valueOf);
		BOXING.put(double.class, Double::valueOf);
		BOXING.put(void.class, x -> null);
	}

	private TypeBoxing() {
	}

	public static Serializable box(Class<?> primitive, String value) {
		assert primitive.isPrimitive();

		return BOXING.get(primitive).apply(value);
	}

}
