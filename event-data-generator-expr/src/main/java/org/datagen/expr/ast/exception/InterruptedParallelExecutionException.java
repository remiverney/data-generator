package org.datagen.expr.ast.exception;

import org.datagen.expr.ast.intf.Node;

public class InterruptedParallelExecutionException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG = "Parallel evaluation thread was interrupted";

	public InterruptedParallelExecutionException(Node node, Throwable cause) {
		super(node, EXCEPTION_MSG, cause);
	}

}
