package org.datagen.connector;

import java.io.Closeable;
import java.io.IOException;

import org.datagen.config.Configurable;
import org.datagen.connector.csv.CsvConnectorParameters;
import org.datagen.dataset.DataRecord;
import org.datagen.extension.PluggableExtension;
import org.datagen.mbean.Manageable;
import org.datagen.utils.Identifiable;
import org.datagen.utils.Observable;

public interface OutputConnector<O extends OutputConnector<O, E>, E extends ConnectorEvent> extends Closeable,
		Observable<O, E>, PluggableExtension, Emitter<DataRecord>, Identifiable<String>, Manageable,
		Configurable<CsvConnectorParameters> {

	default void suspend() {
	}

	default void resume() {
	}

	void open() throws IOException;

	@Override
	void close() throws IOException;
}
