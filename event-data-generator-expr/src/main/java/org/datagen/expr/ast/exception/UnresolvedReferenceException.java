package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;

public class UnresolvedReferenceException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Unresolved reference \"{0}\"";

	private final String reference;
	private final Node node;

	public UnresolvedReferenceException(Node node, String reference) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, reference),
				reference);
	}

	public UnresolvedReferenceException(Node node, String message,
			String reference) {
		super(node, message);
		this.reference = reference;
		this.node = node;
	}

	public String getReference() {
		return reference;
	}

	public Node getNode() {
		return node;
	}
}
