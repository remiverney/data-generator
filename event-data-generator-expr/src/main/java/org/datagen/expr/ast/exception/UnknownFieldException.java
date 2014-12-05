package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class UnknownFieldException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Unknown map field \"{0}\"";

	private final String attribute;

	public UnknownFieldException(Node node, String attribute) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, attribute), attribute);
	}

	private UnknownFieldException(Node node, String message, String attribute) {
		super(node, message);
		this.attribute = attribute;
	}

	public String getAttribute() {
		return attribute;
	}
}
