package org.datagen.expr.ast.format;

import java.util.function.Consumer;

import org.datagen.expr.ast.intf.Node;

public class PrettyExpressionFormatContext implements ExpressionFormatContext {

	private static final String NESTING_EXCEPTION_MSG_PATTERN = "Cannot unnest formatting down to a negative value";
	private static final String SPACING = " ";
	private static final String INDENTATION = "\t";

	private int nesting;

	public PrettyExpressionFormatContext() {
		this.nesting = 0;
	}

	@Override
	public StringBuilder formatKeyword(StringBuilder builder, Enum<?> keyword) {
		return builder.append(keyword.name().toUpperCase());
	}

	@Override
	public StringBuilder formatList(StringBuilder builder,
			Iterable<? extends Node> list, char separator) {
		return formatList(builder, list, separator,
				x -> x.toString(builder, this));
	}

	@Override
	public StringBuilder formatListString(StringBuilder builder,
			Iterable<String> list, char separator) {
		return formatList(builder, list, separator, x -> builder.append(x));
	}

	@Override
	public <T> StringBuilder formatList(StringBuilder builder,
			Iterable<T> list, char separator, Consumer<T> action) {
		boolean first = true;

		for (T item : list) {
			if (!first) {
				builder.append(separator);
				spacing(builder);
			} else {
				first = false;
			}

			action.accept(item);
		}

		return builder;
	}

	@Override
	public StringBuilder spacing(StringBuilder builder) {
		return builder.append(spacing());
	}

	@Override
	public String spacing() {
		return SPACING;
	}

	@Override
	public StringBuilder newline(StringBuilder builder) {
		builder.append('\n');

		for (int i = 0; i < nesting; i++) {
			builder.append(INDENTATION);
		}

		return builder;
	}

	@Override
	public String newline() {
		return newline(new StringBuilder()).toString();
	}

	@Override
	public void nest() {
		nesting++;
	}

	@Override
	public void unnest() {
		nesting--;

		if (nesting < 0) {
			throw new IllegalStateException(NESTING_EXCEPTION_MSG_PATTERN);
		}
	}

}
