package org.datagen.expr.ast.exception;

import java.util.Collection;

import org.datagen.exception.CompoundException;

public class ParsingException extends CompoundException {

	private static final long serialVersionUID = 1L;

	public ParsingException(String message, Collection<ParserException> embedded) {
		super(message, embedded);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ParserException> getEmbedded() {
		return (Collection<ParserException>) super.getEmbedded();
	}

}
