package org.datagen.config;

import java.util.Locale;

import javax.annotation.Nonnull;

public interface I18nResourceManager {

	void setLocale(Locale locale);

	Locale getLocale();

	String getI18n(@Nonnull String key);

	String format(@Nonnull String key, Object... args);

	String format(@Nonnull Enum<? extends I18nCatalogIf> key, Object... args);
}
