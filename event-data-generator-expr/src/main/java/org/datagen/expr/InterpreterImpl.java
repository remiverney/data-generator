package org.datagen.expr;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.ast.DefaultValueFormatContext;
import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.EvalContextImpl;
import org.datagen.expr.ast.PrettyExpressionFormatContext;
import org.datagen.expr.ast.ValueFormatContext;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.DependencyOrder;

public class InterpreterImpl implements Interpreter {

	private static final String COLUMN_NOT_FOUND_MSG = "Unknown colomn '{0}";
	private static final String COLUMN_ALREADY_EXISTS_MSG = "Column '{0}' is already defined";

	private final Map<String, ParserResult> expressions = new HashMap<>();
	private final List<String> sorted = new ArrayList<>();
	private final DateProvider dateProvider;
	private final ValueFormatContext formatContext;
	private final EvalContext context;

	public InterpreterImpl() {
		this(new SystemDateProvider(), new DefaultValueFormatContext());
	}

	public InterpreterImpl(DateProvider dateProvider,
			ValueFormatContext formatContext) {
		this.dateProvider = dateProvider;
		this.formatContext = formatContext;
		this.context = new EvalContextImpl(this.dateProvider,
				this.formatContext);
	}

	@Override
	public void registerExpression(String column, String expression)
			throws CircularDependencyException, UnresolvedDependencyException {
		registerExpressionUnchecked(column, expression);

		updateDependencies();
	}

	@Override
	public void registerExpression(String column, InputStream stream)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException {
		registerExpressionUnchecked(column, stream);

		updateDependencies();
	}

	@Override
	public void registerExpression(String column, Reader reader)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException {
		registerExpressionUnchecked(column, reader);

		updateDependencies();
	}

	@Override
	public void unregisterExpression(String column)
			throws CircularDependencyException, UnresolvedDependencyException {
		if (expressions.remove(column) == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					COLUMN_NOT_FOUND_MSG, column));
		}

		updateDependencies();
	}

	@Override
	public Map<String, Value> eval() {
		return sorted
				.stream()
				.collect(
						Collectors.toMap(
								Function.<String> identity(),
								x -> {

									System.out
											.println("print:"
													+ expressions
															.get(x)
															.getRoot()
															.toString(
																	new StringBuilder(),
																	new PrettyExpressionFormatContext()));

									Value value = expressions.get(x).getRoot()
											.eval(context);
									context.setField(x, value);
									return value;
								}));
	}

	@Override
	public Value eval(String column) {
		ParserResult ast = expressions.get(column);

		if (ast == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					COLUMN_NOT_FOUND_MSG, column));
		}

		return ast.getRoot().eval(context);
	}

	@Override
	public void clear() {
		expressions.clear();
	}

	@Override
	public void setSequence(long sequence) {
		this.context.setSequence(sequence);
	}

	@Override
	public void nextSequence() {
		this.context.nextSequence();
	}

	@Override
	public void registerExpressionsString(Map<String, String> expressions)
			throws CircularDependencyException, UnresolvedDependencyException {
		for (Map.Entry<String, String> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(),
					expression.getValue());
		}

		updateDependencies();
	}

	@Override
	public void registerExpressionsStream(Map<String, InputStream> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException {
		for (Map.Entry<String, InputStream> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(),
					expression.getValue());
		}

		updateDependencies();
	}

	@Override
	public void registerExpressionsReader(Map<String, Reader> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException {
		for (Map.Entry<String, Reader> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(),
					expression.getValue());
		}

		updateDependencies();
	}

	private void registerExpressionUnchecked(String column, String expression) {
		if (expressions.putIfAbsent(column, Parser.parse(expression)) != null) {
			throw new IllegalArgumentException(MessageFormat.format(
					COLUMN_ALREADY_EXISTS_MSG, column));
		}
	}

	private void registerExpressionUnchecked(String column, InputStream stream)
			throws IOException {
		if (expressions.putIfAbsent(column, Parser.parse(stream)) != null) {
			throw new IllegalArgumentException(MessageFormat.format(
					COLUMN_ALREADY_EXISTS_MSG, column));
		}
	}

	private void registerExpressionUnchecked(String column, Reader reader)
			throws IOException {
		if (expressions.putIfAbsent(column, Parser.parse(reader)) != null) {
			throw new IllegalArgumentException(MessageFormat.format(
					COLUMN_ALREADY_EXISTS_MSG, column));
		}
	}

	private void updateDependencies() throws CircularDependencyException,
			UnresolvedDependencyException {
		Map<String, List<String>> dependencies = new HashMap<>();

		for (Map.Entry<String, ParserResult> expr : expressions.entrySet()) {
			dependencies.put(
					expr.getKey(),
					expr.getValue().getReferences().stream()
							.map(x -> x.getReference())
							.collect(Collectors.toCollection(ArrayList::new)));
		}

		sorted.clear();
		sorted.addAll(DependencyOrder.computeOrder(dependencies));
	}

	@Override
	public void registerLibrary(String name, Map<String, String> library) {
		this.context.registerLibrary(
				name,
				library.entrySet()
						.stream()
						.collect(
								Collectors.toMap(
										Map.Entry<String, String>::getKey,
										x -> Parser.parse(x.getValue())
												.getRoot())));
	}

	@Override
	public void unregisterLibrary(String name) {
		this.context.unregisterLibrary(name);
	}

}
