package org.datagen.connector;

import org.datagen.extension.PluggableExtensionFactoryImpl;
import org.datagen.factory.BuilderParameter;

public class ConnectorFactory
		extends
		PluggableExtensionFactoryImpl<OutputConnector<?, ConnectorEvent>, BuilderParameter<?>> {

	public ConnectorFactory() {
		super(OutputConnector.class);
	}

}
