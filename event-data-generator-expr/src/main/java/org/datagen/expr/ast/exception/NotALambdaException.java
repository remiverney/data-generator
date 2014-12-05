package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class NotALambdaException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Invalid call on non-lambda expression of type \"{0}\"";

	private final ValueType type;

	public NotALambdaException(Node node, ValueType type) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, type), type);
	}

	private NotALambdaException(Node node, String message, ValueType type) {
		super(node, message);
		this.type = type;
	}

	public ValueType getType() {
		return type;
	}
}
