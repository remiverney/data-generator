package org.datagen.connector;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

import org.datagen.dataset.DataRecord;
import org.datagen.extension.PluggableExtension;
import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;
import org.datagen.utils.Observable;

public interface OutputConnector<O extends OutputConnector<O, E>, E extends ConnectorEvent>
		extends Closeable, Observable<O, E>, PluggableExtension {

	Config<? extends BuilderParameter<Serializable>> getConfiguration();

	void emit(DataRecord record) throws IOException;

	default void suspend() {
	}

	default void resume() {
	}

	void open() throws IOException;

	@Override
	void close() throws IOException;
}
