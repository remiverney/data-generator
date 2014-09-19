package org.datagen.expr.i18n;

import org.datagen.config.I18n;
import org.datagen.config.I18nCatalog;
import org.datagen.config.I18nCatalogIf;

@I18nCatalog(catalog = ExprI18nCatalog.CATALOG_NAME)
public enum ExprI18nCatalog implements I18nCatalogIf {

	@I18n("Invalid number of arguments when calling function, expected {0}, found {1}")
	BAD_ARGUMENT_NUMBER,

	@I18n("Invalid parsing of expression \"{0}\", first error is : <{1}>")
	INVALID_DYNAMIC_EVALUATION,

	@I18n("Incompatible argument type \"{0}\" for function parameter {1}")
	INCOMPATIBLE_ARGUMENT,

	@I18n("Incompatible attribute \"{0}\" applied on type \"{1}\"")
	INCOMPATIBLE_ATTRIBUTE,

	@I18n("Incompatible argument type \"{0}\" for operator \"{1}\"")
	INCOMPATIBLE_TYPES_UNARY,

	@I18n("Incompatible argument types \"{0}\" and \"{1}\" for operator \"{2}\"")
	INCOMPATIBLE_TYPES_BINARY,

	@I18n("Parallel evaluation thread was interrupted")
	INTERRUPTED_PARALLEL_EXECUTION,

	@I18n("No matching condition case")
	NO_MATCHING_CASE,

	@I18n("No matching case for value \"{0}\"")
	NO_MATCHING_CASE_VALUE,

	@I18n("Non derivable expression node \"{0}\"")
	NON_DERIVABLE_EXPRESSION,

	@I18n("Invalid call on non-lambda expression of type \"{0}\"")
	NOT_A_LAMBDA,

	@I18n("Invalid index access on non-array expression of type \"{0}\"")
	NOT_AN_ARRAY,

	@I18n("Out of bound array access for index {0}, array size is {1}")
	OUT_OF_BOUND_ARRAY_INDEX,

	@I18n("Syntax error [{0}:{1}] near \"{2}\": {3}")
	PARSER_SYNTAX_ERROR,

	@I18n("Lexical error [{0}:{1}]: {2}")
	PARSER_LEXICAL_ERROR,

	@I18n("Unknown expression attribute \"{0}\"")
	UNKNOWN_ATTRIBUTE,

	@I18n("Unknown map field \"{0}\"")
	UNKNOWN_FIELD,

	@I18n("Unresolved reference \"{0}\"")
	UNRESOLVED_REFERENCE;

	protected static final String CATALOG_NAME = "expr-i18n";

}
