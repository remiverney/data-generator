package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;

public class FunctionInvocationException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Invocation of function reference \"{0}\" failed";

	private final String reference;

	public FunctionInvocationException(Node node, String reference, Throwable cause) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, reference), reference, cause);
	}

	public FunctionInvocationException(Node node, String message, String reference, Throwable cause) {
		super(node, message, cause);
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}
}
