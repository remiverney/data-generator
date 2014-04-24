package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;

public class UnknownAttributeException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Unknown expression attribute \"{0}\"";

	private final String attribute;
	private final Node node;

	public UnknownAttributeException(Node node, String attribute) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, attribute),
				attribute);
	}

	public UnknownAttributeException(Node node, String message, String attribute) {
		super(node, message);
		this.attribute = attribute;
		this.node = node;
	}

	public String getAttribute() {
		return attribute;
	}

	public Node getNode() {
		return node;
	}
}
