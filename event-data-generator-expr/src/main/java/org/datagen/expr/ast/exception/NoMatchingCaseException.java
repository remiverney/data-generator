package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class NoMatchingCaseException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN_VALUE = "No matching case for value \"{0}\"";
	private static final String EXCEPTION_MSG_PATTERN = "No matching condition case";

	private final Value value;

	public NoMatchingCaseException(Node node) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, (Value) null),
				null);
	}

	public NoMatchingCaseException(Node node, Value value) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN_VALUE, value),
				value);
	}

	public NoMatchingCaseException(Node node, String message, Value value) {
		super(node, message);
		this.value = value;
	}

	public Value getValue() {
		return value;
	}
}
