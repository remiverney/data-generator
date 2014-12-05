package org.datagen.utils;

import java.io.Serializable;

import javax.annotation.Nonnull;

public interface Identifiable<I extends Serializable> {

	@Nonnull
	I getName();

}
