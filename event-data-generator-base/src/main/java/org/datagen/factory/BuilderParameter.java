package org.datagen.factory;

import java.io.Serializable;

public interface BuilderParameter<T extends Serializable> {

	Class<T> getType();

	T getDefaultValue();
}
