package org.datagen.expr.ast.exception;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public abstract class EvaluationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Node node;

	protected EvaluationException(Node node, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

		this.node = node;
	}

	protected EvaluationException(Node node, String message, Throwable cause) {
		super(message, cause);

		this.node = node;
	}

	protected EvaluationException(Node node, String message) {
		super(message);

		this.node = node;
	}

	public int getLine() {
		return node.getSourceLine();
	}

	public int getCol() {
		return node.getSourceCol();
	}

	public Node getNode() {
		return node;
	}

	@Override
	public String toString() {
		return "[" + getLine() + ":" + getCol() + "]: " + super.toString();
	}
}
