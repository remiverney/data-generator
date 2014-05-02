package org.datagen.expr.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.nodes.FieldRef;
import org.datagen.expr.interpreter.InterpreterParameters;
import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;
import org.datagen.generated.antlr.expression.ExpressionParserBaseListener;
import org.datagen.generated.antlr.expression.ExpressionParserLexer;
import org.datagen.generated.antlr.expression.ExpressionParserParser;
import org.datagen.generated.antlr.expression.ExpressionParserParser.ColumnrefContext;
import org.datagen.generated.antlr.expression.ExpressionParserParser.StartContext;

public class Parser {

	private static class ExpressionParserListener extends
			ExpressionParserBaseListener implements ParserResult {

		private Node root = null;
		private final Collection<FieldRef> references = new ArrayList<>();

		@Override
		public void exitColumnref(ColumnrefContext ctx) {
			references.add(ctx.fieldref);
		}

		@Override
		public void exitStart(StartContext ctx) {
			root = ctx.node;
		}

		@Override
		public Node getRoot() {
			return root;
		}

		@Override
		public Collection<FieldRef> getReferences() {
			return references;
		}
	}

	public static ParserResult parse(String expression) {
		return parse(new ANTLRInputStream(expression), null);
	}

	public static ParserResult parse(InputStream stream) throws IOException {
		return parse(new ANTLRInputStream(stream), null);
	}

	public static ParserResult parse(Reader reader) throws IOException {
		return parse(new ANTLRInputStream(reader), null);
	}

	public static ParserResult parse(String expression,
			Config<InterpreterParameters> configuration) {
		return parse(new ANTLRInputStream(expression), configuration);
	}

	public static ParserResult parse(InputStream stream,
			Config<InterpreterParameters> configuration) throws IOException {
		return parse(new ANTLRInputStream(stream), configuration);
	}

	public static ParserResult parse(Reader reader,
			Config<InterpreterParameters> configuration) throws IOException {
		return parse(new ANTLRInputStream(reader), configuration);
	}

	private static ParserResult parse(ANTLRInputStream stream,
			Config<InterpreterParameters> configuration) {
		ExpressionParserLexer lexer = new ExpressionParserLexer(stream);

		// Get a list of matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Pass the tokens to the parser
		ExpressionParserParser parser = new ExpressionParserParser(tokens);
		parser.setConfiguration(configuration != null ? configuration
				: new ConfigBuilder<InterpreterParameters>().build());

		// Specify our entry point
		StartContext ctx = parser.start();

		// Walk it and attach our listener
		ParseTreeWalker walker = new ParseTreeWalker();
		ExpressionParserListener listener = new ExpressionParserListener();
		walker.walk(listener, ctx);

		return listener;
	}

}
