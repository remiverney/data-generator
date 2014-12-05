package org.datagen.expr.ast.exception;

import java.text.MessageFormat;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class OutBoundsArrayException extends EvaluationException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Out of bound array access for index {0}, array size is {1}";

	private final long size;
	private final long index;

	public OutBoundsArrayException(Node node, long size, long index) {
		this(node, MessageFormat.format(EXCEPTION_MSG_PATTERN, index, size), size, index);
	}

	private OutBoundsArrayException(Node node, String message, long size, long index) {
		super(node, message);
		this.size = size;
		this.index = index;
	}

	public long getSize() {
		return size;
	}

	public long getIndex() {
		return index;
	}
}
