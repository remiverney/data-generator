package org.datagen.expr.ast;

import java.util.function.Consumer;

import org.datagen.expr.ast.intf.Node;

public interface ExpressionFormatContext {

	StringBuilder formatKeyword(StringBuilder builder, Enum<?> keyword);

	StringBuilder formatList(StringBuilder builder,
			Iterable<? extends Node> list, char separator);

	StringBuilder formatListString(StringBuilder builder,
			Iterable<String> list, char separator);

	<T> StringBuilder formatList(StringBuilder builder, Iterable<T> list,
			char separator, Consumer<T> action);

	StringBuilder spacing(StringBuilder builder);

	String spacing();

	StringBuilder newline(StringBuilder builder);

	String newline();

	void nest();

	void unnest();
}
