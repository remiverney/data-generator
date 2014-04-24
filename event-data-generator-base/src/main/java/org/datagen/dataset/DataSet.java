package org.datagen.dataset;

import org.datagen.connector.OutputConnector;

public interface DataSet {

	DataDefinition getDataDefinition();

	OutputConnector getOutputConnector();
}
