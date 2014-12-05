package org.datagen.connector;

import org.datagen.extension.PluggableExtensionFactoryImpl;
import org.datagen.factory.BuilderParameter;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class ConnectorFactory extends
		PluggableExtensionFactoryImpl<OutputConnector<?, ConnectorEvent>, BuilderParameter<?>> {

	public ConnectorFactory() {
		super(OutputConnector.class);
	}

}
