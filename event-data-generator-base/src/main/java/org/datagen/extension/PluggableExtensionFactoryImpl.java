package org.datagen.extension;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;

public class PluggableExtensionFactoryImpl<C extends PluggableExtension, P extends BuilderParameter<?>>
		implements PluggableExtensionFactory<C, P> {

	private static final String DEFAULT_INSTANCE_NAME = "";

	private final Class<? super C> clazz;
	private final Map<String, Class<C>> extensions = new HashMap<>();

	public PluggableExtensionFactoryImpl(Class<? super C> clazz) {
		this(clazz, Thread.currentThread().getContextClassLoader());
	}

	public PluggableExtensionFactoryImpl(Class<? super C> clazz,
			ClassLoader loader) {
		this(clazz, loader, ExtensionLoader::<C> load);
	}

	public PluggableExtensionFactoryImpl(Class<? super C> clazz,
			ExtensionProvider<C> provider) {
		this(clazz, Thread.currentThread().getContextClassLoader(), provider);
	}

	public PluggableExtensionFactoryImpl(Class<? super C> clazz,
			ClassLoader loader, ExtensionProvider<C> provider) {
		this.clazz = clazz;
		registerExtensions(loader, provider);
	}

	private void registerExtensions(ClassLoader loader,
			ExtensionProvider<C> provider) {
		@SuppressWarnings("unchecked")
		Iterable<Class<C>> extensions = provider.provide((Class<C>) this.clazz,
				loader);

		for (Class<C> extension : extensions) {
			if (!extension.isAnnotationPresent(Extension.class)) {
				continue;
			}

			Extension annotation = extension.getAnnotation(Extension.class);
			this.extensions.put(annotation.type(), extension);
		}
	}

	@Override
	public C get(Config<P> config) {
		if (this.extensions.size() > 1) {
			throw new IllegalStateException(
					MessageFormat
							.format("Cannot get extension instance for class [ {0} ]: more than one implementation registered",
									this.clazz.getName()));
		}

		return get(this.extensions.values().iterator().next(),
				DEFAULT_INSTANCE_NAME, config);
	}

	@Override
	public ConfigBuilder<P> getConfigBuilder() {
		return new ConfigBuilder<P>();
	}

	@Override
	public C get(String type, String name, Config<P> config) {
		return get(this.extensions.get(type), name, config);
	}

	private C get(Class<C> clazz, String name, Config<P> config) {
		try {
			return clazz.getConstructor(String.class, Config.class)
					.newInstance(name, config);
		} catch (InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e) {
			throw new IllegalArgumentException(
					MessageFormat.format(
							"Could not invoke constructor to instantiate class [ {0} ] with arguments (String, Config)",
							clazz.getName()), e);
		}
	}

	@Override
	public Collection<String> getTypes() {
		return this.extensions.keySet();
	}

}
