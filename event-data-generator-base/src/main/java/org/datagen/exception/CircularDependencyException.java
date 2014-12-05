package org.datagen.exception;

import java.text.MessageFormat;
import java.util.List;

public class CircularDependencyException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN = "Circular dependency detected, closed path is [ {0} ]";

	private final List<?> path;

	public CircularDependencyException(List<?> path) {
		super(MessageFormat.format(EXCEPTION_MSG_PATTERN, formatPath(path)));
		this.path = path;
	}

	public CircularDependencyException(List<Object> path, Throwable cause) {
		super(MessageFormat.format(EXCEPTION_MSG_PATTERN, formatPath(path)), cause);
		this.path = path;
	}

	public List<?> getPath() {
		return path;
	}

	private static String formatPath(List<?> path) {
		return path.stream().sequential().reduce(new StringBuilder(), (t, u) -> {
			return t.append(u).append("->");
		}, (u, v) -> {
			return u.append(v);
		}).toString();
	}
}
