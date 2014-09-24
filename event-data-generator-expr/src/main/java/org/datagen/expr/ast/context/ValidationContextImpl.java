package org.datagen.expr.ast.context;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.datagen.expr.ast.exception.EvaluationException;
import org.datagen.expr.ast.intf.Node;

public class ValidationContextImpl implements ValidationContext {

	private static class StatusImpl implements Status {

		private static final long serialVersionUID = 1L;

		private static final String STATUS_FORMAT = "{0}: [{1}:{2}]: {3}";

		private final StatusLevel level;
		private final EvaluationException exception;

		public StatusImpl(StatusLevel level, EvaluationException exception) {
			this.level = level;
			this.exception = exception;
		}

		@Override
		public StatusLevel getLevel() {
			return level;
		}

		@Override
		public EvaluationException getException() {
			return exception;
		}

		@Override
		public String getMessage() {
			return exception.getMessage();
		}

		@Override
		public Node getNode() {
			return exception.getNode();
		}

		@Override
		public int getLine() {
			return exception.getLine();
		}

		@Override
		public int getColumn() {
			return exception.getCol();
		}

		@Override
		public String format(String pattern) {
			return MessageFormat.format(pattern, getLevel(), getLine(), getColumn(), getMessage());
		}

		@Override
		public String toString() {
			return format(STATUS_FORMAT);
		}

	}

	private static final long serialVersionUID = 1L;

	private final List<Status> statuses = new ArrayList<>();

	public ValidationContextImpl() {
	}

	@Override
	public void addStatus(StatusLevel level, EvaluationException exception) {
		addStatus(new StatusImpl(level, exception));
	}

	@Override
	public void addStatus(Status status) {
		this.statuses.add(status);
	}

	@Override
	public Collection<Status> getStatuses() {
		return this.statuses;
	}

	@Override
	public Collection<Status> getStatuses(StatusLevel min, boolean sorted) {
		Stream<Status> filtered = this.statuses.stream();

		if (min != null) {
			filtered = filtered.filter(s -> (s.getLevel().compareTo(min) <= 0));
		}

		return (sorted ? filtered.sorted() : filtered).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void clear() {
		this.statuses.clear();
	}

	@Override
	public boolean isValid(StatusLevel max) {
		return this.statuses.stream().allMatch(s -> (s.getLevel().compareTo(max) >= 0));
	}

	@Override
	public String format(String pattern) {
		StringBuilder builder = new StringBuilder();

		statuses.stream().forEachOrdered(s -> builder.append(s.format(pattern)).append('\n'));

		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		statuses.stream().forEachOrdered(s -> builder.append(s).append('\n'));

		return builder.toString();
	}

}
