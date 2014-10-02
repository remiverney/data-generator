package org.datagen.config;

public interface I18nCatalogIf {

	default String getCatalogName() {
		return getClass().isAnnotationPresent(I18nCatalog.class) ? getClass().getAnnotation(I18nCatalog.class)
				.catalog() : getClass().getSimpleName();
	}

	default String key() {
		return toString();
	}

	default String getDefault() {
		try {
			return this.getClass().getDeclaredField(key()).getAnnotation(I18n.class).value();
		} catch (NoSuchFieldException | SecurityException | NullPointerException e) {
			return this.getClass().getName() + "." + key();
		}
	}
}
