package org.datagen.expr.ast.exception;

import java.text.MessageFormat;
import java.util.Optional;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class IncompatibleArgumentException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Incompatible argument type \"{0}\" for function parameter {1}";
	private static final String EXCEPTION_MSG_PATTERN_2 = "Incompatible argument type \"{0}\" for function parameter {1}, expected \"{2}\"";

	private final int parameter;
	private final ValueType type;
	private final Optional<ValueType> expected;

	public IncompatibleArgumentException(Node node, int parameter, ValueType type) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, type, parameter), parameter, type, Optional
				.<ValueType> empty());
	}

	public IncompatibleArgumentException(Node node, int parameter, ValueType type, Throwable cause) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, type, parameter), parameter, type, Optional
				.<ValueType> empty(), cause);
	}

	public IncompatibleArgumentException(Node node, int parameter, ValueType type, ValueType expected) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN_2, type, parameter, expected), parameter, type, Optional
				.of(expected));
	}

	private IncompatibleArgumentException(Node node, String message, int parameter, ValueType type,
			Optional<ValueType> expected) {
		super(node, message);
		this.parameter = parameter;
		this.type = type;
		this.expected = expected;
	}

	private IncompatibleArgumentException(Node node, String message, int parameter, ValueType type,
			Optional<ValueType> expected, Throwable cause) {
		super(node, message, cause);
		this.parameter = parameter;
		this.type = type;
		this.expected = expected;
	}

	public int getParameter() {
		return parameter;
	}

	public ValueType getType() {
		return type;
	}

	public Optional<ValueType> getExpected() {
		return expected;
	}
}
