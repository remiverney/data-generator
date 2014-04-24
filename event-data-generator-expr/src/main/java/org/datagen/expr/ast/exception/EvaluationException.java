package org.datagen.expr.ast.exception;

import org.datagen.expr.ast.intf.Node;

public abstract class EvaluationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int line;
	private final int col;

	protected EvaluationException(Node node, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

		this.line = node.getSourceLine();
		this.col = node.getSourceCol();
	}

	protected EvaluationException(Node node, String message, Throwable cause) {
		super(message, cause);

		this.line = node.getSourceLine();
		this.col = node.getSourceCol();
	}

	protected EvaluationException(Node node, String message) {
		super(message);

		this.line = node.getSourceLine();
		this.col = node.getSourceCol();
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	@Override
	public String toString() {
		return "[" + line + ":" + col + "]: " + super.toString();
	}
}
