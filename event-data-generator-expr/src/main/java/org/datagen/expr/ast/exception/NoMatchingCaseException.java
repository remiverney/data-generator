package org.datagen.expr.ast.exception;

import java.text.MessageFormat;
import java.util.Optional;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class NoMatchingCaseException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN_VALUE = "No matching case for value \"{0}\"";
	private static final String EXCEPTION_MSG_PATTERN = "No matching condition case";

	private final Optional<Value> value;

	public NoMatchingCaseException(Node node) {
		this(node, EXCEPTION_MSG_PATTERN, Optional.<Value> empty());
	}

	public NoMatchingCaseException(Node node, Value value) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN_VALUE, value), Optional.of(value));
	}

	private NoMatchingCaseException(Node node, String message, Optional<Value> value) {
		super(node, message);
		this.value = value;
	}

	public Optional<Value> getValue() {
		return value;
	}
}
