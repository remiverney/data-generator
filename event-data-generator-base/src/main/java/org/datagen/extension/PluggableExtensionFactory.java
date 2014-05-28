package org.datagen.extension;

import java.util.Collection;

import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;
import org.datagen.factory.GenericFactory;

public interface PluggableExtensionFactory<C extends PluggableExtension, P extends BuilderParameter<?>>
		extends GenericFactory<C, P> {

	C get(String type, String name, Config<P> config);

	Collection<String> getTypes();

}
