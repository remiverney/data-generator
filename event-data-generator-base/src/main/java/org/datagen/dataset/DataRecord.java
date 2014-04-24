package org.datagen.dataset;

import java.io.Serializable;

public interface DataRecord {

	DataDefinition getDefinition();

	Serializable[] getValues();
}
