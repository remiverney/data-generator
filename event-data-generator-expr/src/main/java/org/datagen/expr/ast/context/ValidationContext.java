package org.datagen.expr.ast.context;

import org.datagen.expr.ast.exception.EvaluationException;

public interface ValidationContext extends ValidationResult {

	void addStatus(StatusLevel level, EvaluationException exception);

	void addStatus(Status status);

	void clear();
}
