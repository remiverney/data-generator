package org.datagen.utils;

@FunctionalInterface
public interface VarArgFunction<T, U> {

	U apply(@SuppressWarnings("unchecked") T... t);
}
