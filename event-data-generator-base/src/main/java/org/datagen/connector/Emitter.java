package org.datagen.connector;

import java.io.IOException;
import java.io.Serializable;

public interface Emitter<T extends Serializable> {

	void emit(T record) throws IOException;

}
