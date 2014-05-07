package org.datagen.exception;

import java.util.Collection;

public class CompoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private final Collection<? extends Exception> embedded;

	public CompoundException(String message,
			Collection<? extends Exception> embedded) {
		super(message);
		this.embedded = embedded;
	}

	public Collection<? extends Exception> getEmbedded() {
		return embedded;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(":\n");

		for (Exception e : embedded) {
			builder.append("\t" + e.getMessage() + "\n");
		}

		return builder.toString();
	}

}
