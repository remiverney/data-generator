package org.datagen.expr.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Optional;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.ParserException;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.nodes.FieldRef;
import org.datagen.expr.interpreter.InterpreterParameters;
import org.datagen.factory.Config;
import org.datagen.generated.antlr.expression.ExpressionParserBaseListener;
import org.datagen.generated.antlr.expression.ExpressionParserLexer;
import org.datagen.generated.antlr.expression.ExpressionParserParser;
import org.datagen.generated.antlr.expression.ExpressionParserParser.ColumnrefContext;
import org.datagen.generated.antlr.expression.ExpressionParserParser.StartContext;

public class Parser {

	private static class ExpressionParserListener extends ExpressionParserBaseListener implements ParserResult {

		private Optional<Node> root = Optional.empty();
		private final Collection<FieldRef> references = new ArrayList<>();

		@Override
		public void exitColumnref(ColumnrefContext ctx) {
			references.add(ctx.fieldref);
		}

		@Override
		public void exitStart(StartContext ctx) {
			root = Optional.of(ctx.node);
		}

		@Override
		public Node getRoot() {
			return root.get();
		}

		@Override
		public Collection<FieldRef> getReferences() {
			return references;
		}
	}

	private static class ParserErrorListener implements ANTLRErrorListener {

		private final Collection<ParserException> errors = new ArrayList<>();

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			ParserException error = new ParserException(line, charPositionInLine,
					offendingSymbol != null ? ((Token) offendingSymbol).getText() : null, msg, e);

			errors.add(error);
		}

		@Override
		public void reportAmbiguity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex,
				boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		}

		@Override
		public void reportAttemptingFullContext(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex,
				int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
		}

		@Override
		public void reportContextSensitivity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex,
				int stopIndex, int prediction, ATNConfigSet configs) {
		}

		public Collection<ParserException> getErrors() {
			return errors;
		}

	}

	public static ParserResult parse(String expression, EvalContext context, Optional<ClassLoader> loader)
			throws ParsingException {
		return parse(new ANTLRInputStream(expression), Optional.<Config<InterpreterParameters>> empty(), context,
				loader);
	}

	public static ParserResult parse(InputStream stream, EvalContext context, Optional<ClassLoader> loader)
			throws IOException, ParsingException {
		return parse(new ANTLRInputStream(stream), Optional.<Config<InterpreterParameters>> empty(), context, loader);
	}

	public static ParserResult parse(Reader reader, EvalContext context, Optional<ClassLoader> loader)
			throws IOException, ParsingException {
		return parse(new ANTLRInputStream(reader), Optional.<Config<InterpreterParameters>> empty(), context, loader);
	}

	public static ParserResult parse(String expression, Optional<Config<InterpreterParameters>> configuration,
			EvalContext context, Optional<ClassLoader> loader) throws ParsingException {
		return parse(new ANTLRInputStream(expression), configuration, context, loader);
	}

	public static ParserResult parse(InputStream stream, Optional<Config<InterpreterParameters>> configuration,
			EvalContext context, Optional<ClassLoader> loader) throws IOException, ParsingException {
		return parse(new ANTLRInputStream(stream), configuration, context, loader);
	}

	public static ParserResult parse(Reader reader, Optional<Config<InterpreterParameters>> configuration,
			EvalContext context, Optional<ClassLoader> loader) throws IOException, ParsingException {
		return parse(new ANTLRInputStream(reader), configuration, context, loader);
	}

	private static ParserResult parse(ANTLRInputStream stream, Optional<Config<InterpreterParameters>> configuration,
			EvalContext context, Optional<ClassLoader> loader) throws ParsingException {
		ExpressionParserLexer lexer = new ExpressionParserLexer(stream);

		// Get a list of matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Pass the tokens to the parser
		ExpressionParserParser parser = new ExpressionParserParser(tokens);
		parser.setConfiguration(Config.ensure(configuration));
		parser.setEvalContext(context);
		parser.setJavaRefClassLoader(loader);

		ParserErrorListener errorListener = new ParserErrorListener();
		parser.addErrorListener(errorListener);
		lexer.addErrorListener(errorListener);

		// Specify our entry point
		StartContext ctx = parser.start();

		// Walk it and attach our listener
		ParseTreeWalker walker = new ParseTreeWalker();
		ExpressionParserListener listener = new ExpressionParserListener();
		walker.walk(listener, ctx);

		if (!errorListener.getErrors().isEmpty()) {
			throw new ParsingException("Expression parsing error", errorListener.getErrors());
		}

		return listener;
	}
}
