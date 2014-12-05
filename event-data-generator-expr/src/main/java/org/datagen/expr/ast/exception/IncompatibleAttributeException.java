package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class IncompatibleAttributeException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Incompatible attribute \"{0}\" applied on type \"{1}\"";

	private final ValueType type;
	private final String attribute;

	public IncompatibleAttributeException(Node node, ValueType type, String attribute) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, attribute, type), attribute, type);
	}

	private IncompatibleAttributeException(Node node, String message, String attribute, ValueType type) {
		super(node, message);
		this.attribute = attribute;
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public ValueType getType() {
		return type;
	}
}
