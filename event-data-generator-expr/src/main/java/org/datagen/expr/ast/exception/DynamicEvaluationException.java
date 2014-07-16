package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;

public class DynamicEvaluationException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Invalid parsing of expression \"{0}\", first error is : <{1}>";

	private final String expression;

	public DynamicEvaluationException(Node node, String expression,
			ParsingException cause) {
		super(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, expression,
				cause.getEmbedded().iterator().next().getMessage()), cause);
		this.expression = expression;
	}

	public String getExpression() {
		return this.expression;
	}
}
