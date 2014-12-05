package org.datagen.config;

import java.util.Map;

import javax.annotation.Nonnull;

public interface ConfigManager {

	void load(@Nonnull String path);

	void reset();

	int getIntParameter(@Nonnull String name, int defValue);

	float getFloatParameter(@Nonnull String name, float defValue);

	boolean getBooleanParameter(@Nonnull String name, boolean defValue);

	String getStringParameter(@Nonnull String name, String defValue);

	void setIntParameter(@Nonnull String name, int value);

	void setFloatParameter(@Nonnull String name, float value);

	void setBooleanParameter(@Nonnull String name, boolean value);

	void setStringParameter(@Nonnull String name, String value);

	Map<String, String> getParameters();
}
