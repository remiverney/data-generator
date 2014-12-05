package org.datagen.expr.ast.exception;

import java.text.MessageFormat;
import java.util.Optional;

import org.datagen.utils.annotation.Immutable;

@Immutable
public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MSG_PATTERN_SYNTAX = "Syntax error [{0}:{1}] near \"{2}\": {3}";
	private static final String EXCEPTION_MSG_PATTERN_LEXICAL = "Lexical error [{0}:{1}]: {2}";

	private final int line;
	private final int col;
	private final String token;
	private final String description;

	public ParserException(int line, int col, String token, String description, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(buildMessage(line, col, Optional.<String> ofNullable(token), description), cause, enableSuppression,
				writableStackTrace);
		this.line = line;
		this.col = col;
		this.token = token;
		this.description = description;
	}

	public ParserException(int line, int col, String token, String description, Throwable cause) {
		super(buildMessage(line, col, Optional.<String> ofNullable(token), description), cause);
		this.line = line;
		this.col = col;
		this.token = token;
		this.description = description;
	}

	private static String buildMessage(int line, int col, Optional<String> token, String description) {
		if (token.isPresent()) {
			return MessageFormat.format(EXCEPTION_MSG_PATTERN_SYNTAX, line, col, token, description);
		} else {
			return MessageFormat.format(EXCEPTION_MSG_PATTERN_LEXICAL, line, col, description);
		}
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	public String getToken() {
		return token;
	}

	public String getDescription() {
		return description;
	}
}
