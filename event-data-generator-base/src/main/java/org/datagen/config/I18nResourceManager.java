package org.datagen.config;

import java.util.Locale;

public interface I18nResourceManager {

	void setLocale(Locale locale);

	Locale getLocale();

	String getI18n(String key);

	String format(String key, Object... args);

	String format(Enum<? extends I18nCatalogIf> key, Object... args);
}
