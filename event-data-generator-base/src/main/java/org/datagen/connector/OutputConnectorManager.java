package org.datagen.connector;

import java.io.Closeable;
import java.util.Map;

public interface OutputConnectorManager extends Closeable {

	Map<String, OutputConnector> getOutputConnectors();

	OutputConnector getOutputConnector(String name);
}
