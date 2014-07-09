package org.datagen.utils;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface CallableExt<V, E extends Exception> extends Callable<V>{

	@Override
	V call() throws E;
}
