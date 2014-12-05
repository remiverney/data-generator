package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class BadArgumentsNumberException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Invalid number of arguments when calling function, expected {0}, found {1}";

	private final long size;
	private final long expected;

	public BadArgumentsNumberException(Node node, long size, long expected) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, expected, size), size, expected);
	}

	private BadArgumentsNumberException(Node node, String message, long size, long expected) {
		super(node, message);
		this.size = size;
		this.expected = expected;
	}

	public long getSize() {
		return size;
	}

	public long getExpected() {
		return expected;
	}
}
