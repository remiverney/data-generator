package org.datagen.extension;

@FunctionalInterface
public interface ExtensionProvider<E> {

	Iterable<Class<E>> provide(Class<E> clazz, ClassLoader loader);

}
