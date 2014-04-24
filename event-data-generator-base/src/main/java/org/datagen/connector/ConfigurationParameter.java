package org.datagen.connector;

public interface ConfigurationParameter<T> {

	String getName();

	T getDefaultValue();

	T getMin();

	T getMax();

	boolean required();

	FormInput getFormInput();
}
