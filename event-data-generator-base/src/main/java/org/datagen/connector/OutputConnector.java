package org.datagen.connector;

import java.io.Closeable;
import java.io.IOException;

import org.datagen.dataset.DataRecord;

public interface OutputConnector extends Closeable {

	String getName();

	ConfigurationMetadata getConfigurationMetadata();

	Configuration getConfiguration();

	void emit(DataRecord record) throws IOException;

	void suspend();

	void resume();

	void open() throws IOException;

	@Override
	void close() throws IOException;

	ConnectorState getState();
}
