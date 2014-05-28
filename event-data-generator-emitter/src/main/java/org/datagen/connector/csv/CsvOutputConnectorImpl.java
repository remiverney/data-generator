package org.datagen.connector.csv;

import java.io.IOException;

import org.datagen.connector.OutputConnector;
import org.datagen.dataset.DataRecord;
import org.datagen.extension.Extension;
import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;
import org.datagen.utils.ObservableBase;

@Extension(type = "csv", description = "output to CSV file")
public class CsvOutputConnectorImpl extends
		ObservableBase<CsvOutputConnectorImpl, CsvConnectorEvent> implements
		OutputConnector<CsvOutputConnectorImpl, CsvConnectorEvent> {

	private static final String DEFAULT_NAME = "default";

	private final Config<CsvConnectorParameters> configuration;
	private final String name;
	private CsvFileManager fileManager;

	public CsvOutputConnectorImpl() {
		this(DEFAULT_NAME, new ConfigBuilder<CsvConnectorParameters>().build());
	}

	public CsvOutputConnectorImpl(String name,
			Config<CsvConnectorParameters> configuration) {
		this.name = name;
		this.configuration = configuration;
		this.fileManager = null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Config<CsvConnectorParameters> getConfiguration() {
		return this.configuration;
	}

	@Override
	public void emit(DataRecord record) throws IOException {
		this.fileManager.serialize(record);
	}

	@Override
	public void open() throws IOException {
		this.fileManager = new CsvFileManager(
				this.configuration,
				this.name,
				new CsvRecordSerializer(
						this.configuration
								.<String> get(CsvConnectorParameters.FIELD_SEPARATOR),
						this.configuration
								.<String> get(CsvConnectorParameters.NULL_SERIALIZATION)));
	}

	@Override
	public void close() throws IOException {
		if (this.fileManager != null) {
			this.fileManager.close();
			this.fileManager = null;
		}
	}

}
