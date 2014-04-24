package org.datagen.exception;

import java.text.MessageFormat;

public class UnresolvedDependencyException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Unresolved dependency detected [ {0} ], referenced by [ {1} ]";

	private final String dependency;
	private final String dependent;

	public UnresolvedDependencyException(String dependency, String dependent) {
		super(MessageFormat
				.format(EXCEPTION_MSG_PATTERN, dependency, dependent));
		this.dependency = dependency;
		this.dependent = dependent;
	}

	public String getDependency() {
		return dependency;
	}

	public String getDependent() {
		return dependent;
	}
}
