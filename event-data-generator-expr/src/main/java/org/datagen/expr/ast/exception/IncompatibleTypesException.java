package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class IncompatibleTypesException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN_1 = "Incompatible argument type \"{0}\" for operator \"{1}\"";
	private static final String EXCEPTION_MSG_PATTERN_2 = "Incompatible argument types \"{0}\" and \"{1}\" for operator \"{2}\"";

	private final Operator<?> operator;
	private final ValueType lhs;
	private final ValueType rhs;

	public IncompatibleTypesException(Node node, Operator<?> operator, Value lhs, Value rhs) {
		this(node, operator, lhs.getType(), rhs.getType());
	}

	public IncompatibleTypesException(Node node, Operator<?> operator, ValueType lhs, ValueType rhs) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN_2, lhs, rhs, operator.getSymbol()), operator, lhs, rhs);
	}

	public IncompatibleTypesException(Node node, Operator<?> operator, Value rhs) {
		this(node, operator, rhs.getType());
	}

	public IncompatibleTypesException(Node node, Operator<?> operator, ValueType rhs) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN_1, rhs, operator.getSymbol()), operator, null, rhs);
	}

	private IncompatibleTypesException(Node node, String message, Operator<?> operator, ValueType lhs, ValueType rhs) {
		super(node, message);
		this.operator = operator;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Operator<?> getOperator() {
		return operator;
	}

	public ValueType getLhs() {
		return lhs;
	}

	public ValueType getRhs() {
		return rhs;
	}
}
