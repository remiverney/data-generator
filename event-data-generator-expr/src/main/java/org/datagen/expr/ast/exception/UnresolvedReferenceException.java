package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class UnresolvedReferenceException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Unresolved reference \"{0}\"";

	private final String reference;

	public UnresolvedReferenceException(Node node, String reference) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, reference), reference);
	}

	private UnresolvedReferenceException(Node node, String message, String reference) {
		super(node, message);
		this.reference = reference;
	}

	public UnresolvedReferenceException(Node node, String reference, Throwable cause) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, reference), reference, cause);
	}

	private UnresolvedReferenceException(Node node, String message, String reference, Throwable cause) {
		super(node, message, cause);
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}
}
