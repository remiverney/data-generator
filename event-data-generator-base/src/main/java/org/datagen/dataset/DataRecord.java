package org.datagen.dataset;

import java.io.Serializable;

public interface DataRecord extends Serializable {

	DataDefinition getDefinition();

	Serializable[] getValues();
}
