package org.datagen.expr.ast.context;

import java.io.Serializable;
import java.util.Collection;

import org.datagen.expr.ast.exception.EvaluationException;
import org.datagen.expr.ast.intf.Node;

public interface ValidationResult extends Serializable {

	enum StatusLevel {
		ERROR,
		WARNING,
		INFO
	}

	interface Status extends Serializable {
		StatusLevel getLevel();

		EvaluationException getException();

		String getMessage();

		Node getNode();

		int getLine();

		int getColumn();

		String format(String pattern);
	}

	Collection<Status> getStatuses();

	Collection<Status> getStatuses(StatusLevel min, boolean sorted);

	boolean isValid(StatusLevel max);

	String format(String pattern);

}