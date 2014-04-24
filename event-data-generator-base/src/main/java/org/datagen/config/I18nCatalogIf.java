package org.datagen.config;

public interface I18nCatalogIf {

	default String getCatalogName() {
		return getClass().isAnnotationPresent(I18nCatalog.class) ? getClass()
				.getAnnotation(I18nCatalog.class).catalog() : getClass()
				.getSimpleName();
	}

	default String key() {
		return toString();
	}
}
