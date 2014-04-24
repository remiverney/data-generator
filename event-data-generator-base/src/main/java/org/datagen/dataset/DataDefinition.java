package org.datagen.dataset;

import java.util.Map;

public interface DataDefinition {

	Map<String, DataFieldDefinition> getFieldsMap();

	DataFieldDefinition[] getFields();

	DataFieldDefinition getField(String name);

	int getNbFields();
}
