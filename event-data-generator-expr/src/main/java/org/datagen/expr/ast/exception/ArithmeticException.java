package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;

public class ArithmeticException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Arithmetic error for operator \"{0}\": \"{1}\"";

	private final Operator<?> operator;
	private final Value lhs;
	private final Value rhs;

	public ArithmeticException(Node node, Operator<?> operator, Value lhs, Value rhs, Exception cause) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, operator.getSymbol(), cause.getMessage()), operator,
				lhs, rhs, cause);
	}

	public ArithmeticException(Node node, String message, Operator<?> operator, Value lhs, Value rhs, Exception cause) {
		super(node, message, cause);
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Operator<?> getOperator() {
		return operator;
	}

	public Value getLhs() {
		return lhs;
	}

	public Value getRhs() {
		return rhs;
	}
}
