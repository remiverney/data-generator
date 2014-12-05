package org.datagen.connector;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Nonnull;

public interface Emitter<T extends Serializable> {

	void emit(@Nonnull T record) throws IOException;

}
