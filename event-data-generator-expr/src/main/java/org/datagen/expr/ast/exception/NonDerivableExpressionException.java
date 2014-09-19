package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;

public class NonDerivableExpressionException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Non derivable expression node \"{0}\"";

	public NonDerivableExpressionException(Node node) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, node.getClass().getName()));
	}

	public NonDerivableExpressionException(Node node, String message) {
		super(node, message);
	}
}
