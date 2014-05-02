package org.datagen.factory;

import java.io.Serializable;

public interface Config<P> {

	<C extends Serializable> C get(P parameter);

	boolean isEnabled(P parameter);
}
