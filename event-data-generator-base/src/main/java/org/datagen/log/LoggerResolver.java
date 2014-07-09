package org.datagen.log;

@FunctionalInterface
public interface LoggerResolver<L> {

	L resolve(String name, LoggerFactory factory);
}
