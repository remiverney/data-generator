package org.datagen.expr.i18n;

import org.datagen.config.I18n;
import org.datagen.config.I18nCatalog;
import org.datagen.config.I18nCatalogIf;

@I18nCatalog(catalog = ExprI18nCatalog.CATALOG_NAME)
public enum ExprI18nCatalog implements I18nCatalogIf {

	@I18n("Invalid number of arguments when calling function, expected {0}, found {1}")
	BAD_ARGUMENT_NUMBER,

	@I18n("Incompatible argument type \"{0}\" for function parameter {1}")
	INCOMPATIBLE_FUNCTION_ARGUMENT;

	protected static final String CATALOG_NAME = "expr-i18n";

}
