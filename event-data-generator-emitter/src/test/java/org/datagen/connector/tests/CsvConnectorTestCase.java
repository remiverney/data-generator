package org.datagen.connector.tests;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.datagen.connector.ConnectorEvent;
import org.datagen.connector.ConnectorFactory;
import org.datagen.connector.OutputConnector;
import org.datagen.connector.csv.CsvConnectorParameters;
import org.datagen.dataset.DataDefinition;
import org.datagen.dataset.DataRecord;
import org.datagen.factory.BuilderParameter;
import org.datagen.factory.ConfigBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CsvConnectorTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		ConnectorFactory factory = new ConnectorFactory();
		ConfigBuilder<BuilderParameter<?>> config = factory.getConfigBuilder();
		config.set(CsvConnectorParameters.FILE_DIRECTORY, new File(".\\target"));
		try (OutputConnector<?, ConnectorEvent> connector = factory.get("csv", "mycsv", config.build())) {

			DataRecord record = new DataRecord() {

				private static final long serialVersionUID = 1L;

				@Override
				public DataDefinition getDefinition() {
					return null;
				}

				@Override
				public Serializable[] getValues() {
					return new Serializable[] { 12, "azerty", true };
				}

			};
			connector.open();
			connector.emit(record);
			connector.emit(record);
		}
	}

}
