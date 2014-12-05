package org.datagen.connector.csv.nio;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;

import org.datagen.connector.OutputConnector;
import org.datagen.connector.csv.CsvConnectorEvent;
import org.datagen.connector.csv.CsvConnectorParameters;
import org.datagen.dataset.DataRecord;
import org.datagen.extension.Extension;
import org.datagen.factory.Config;
import org.datagen.mbean.MBeanAttributeHelper;
import org.datagen.utils.ObservableBase;

@Extension(type = "csv", description = "output to CSV file")
public class CsvOutputConnectorImpl extends ObservableBase<CsvOutputConnectorImpl, CsvConnectorEvent> implements
		OutputConnector<CsvOutputConnectorImpl, CsvConnectorEvent> {

	private static final String DEFAULT_NAME = "default";

	private final Optional<Config<CsvConnectorParameters>> configuration;
	private final String name;
	private CsvNioFileManager fileManager;

	public CsvOutputConnectorImpl() {
		this(DEFAULT_NAME);
	}

	public CsvOutputConnectorImpl(String name) {
		this(name, Optional.<Config<CsvConnectorParameters>> empty());
	}

	public CsvOutputConnectorImpl(String name, Optional<Config<CsvConnectorParameters>> configuration) {
		this.name = name;
		this.configuration = configuration;
		this.fileManager = null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Optional<Config<CsvConnectorParameters>> getConfiguration() {
		return this.configuration;
	}

	@Override
	public void emit(DataRecord record) throws IOException {
		this.fileManager.serialize(record);
	}

	@Override
	public void open() throws IOException {
		Config<CsvConnectorParameters> config = Config.ensure(this.configuration);

		this.fileManager = new CsvNioFileManager(config, this.name, new CsvNioRecordSerializer(config.<String> get(
				CsvConnectorParameters.FIELD_SEPARATOR).get(), config.<String> get(
				CsvConnectorParameters.NULL_SERIALIZATION).get(), config.<String> get(
				CsvConnectorParameters.LINE_SEPARATOR).get()));
	}

	@Override
	public void close() throws IOException {
		if (this.fileManager != null) {
			this.fileManager.close();
			this.fileManager = null;
		}
	}

	@Override
	public String getObjectInstanceName() {
		return getName();
	}

	@Override
	public OpenMBeanAttributeInfoSupport[] getAttributeInfo() {
		return new OpenMBeanAttributeInfoSupport[] { new OpenMBeanAttributeInfoSupport("config",
				"Output connector configuration settings", MBeanAttributeHelper.TABLE_TYPE, true, false, false) };
	}

	@Override
	public Object getAttribute(String name) throws AttributeNotFoundException {
		Supplier<?> supplier;

		switch (name) {
		case ATTR_CONFIGURATION:
			supplier = () -> MBeanAttributeHelper.buildConfigurationData(CsvOutputConnectorImpl.this);
			break;
		default:
			throw new AttributeNotFoundException(name);
		}

		return supplier.get();
	}

}
