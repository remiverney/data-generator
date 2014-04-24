package org.datagen.config;

import java.util.Map;

public interface ConfigManager {

	void load(String path);

	void reset();

	int getIntParameter(String name, int defValue);

	float getFloatParameter(String name, float defValue);

	boolean getBooleanParameter(String name, boolean defValue);

	String getStringParameter(String name, String defValue);

	void setIntParameter(String name, int value);

	void setFloatParameter(String name, float value);

	void setBooleanParameter(String name, boolean value);

	void setStringParameter(String name, String value);

	Map<String, String> getParameters();
}
