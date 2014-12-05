package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class IncompatibleFunctionReturnTypeException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Incompatible returned type \"{0}\" of function \"{1}\"";

	private final String reference;
	private final Class<?> clazz;

	public IncompatibleFunctionReturnTypeException(Node node, String reference, Class<?> clazz) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, clazz.getName(), reference), reference, clazz);
	}

	private IncompatibleFunctionReturnTypeException(Node node, String message, String reference, Class<?> clazz) {
		super(node, message);
		this.reference = reference;
		this.clazz = clazz;
	}

	public String getReference() {
		return reference;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
