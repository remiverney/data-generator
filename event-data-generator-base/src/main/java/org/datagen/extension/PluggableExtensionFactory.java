package org.datagen.extension;

import java.util.Collection;
import java.util.Optional;

import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;
import org.datagen.factory.GenericFactory;

public interface PluggableExtensionFactory<C extends PluggableExtension, P extends BuilderParameter<?>> extends
		GenericFactory<String, C, P> {

	C get(String type, String name, Optional<Config<P>> config);

	Collection<String> getTypes();

}
