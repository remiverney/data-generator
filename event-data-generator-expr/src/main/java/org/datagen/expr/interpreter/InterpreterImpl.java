package org.datagen.expr.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.DateProvider;
import org.datagen.expr.SystemDateProvider;
import org.datagen.expr.ast.AstWalker;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.EvalContextImpl;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationContextImpl;
import org.datagen.expr.ast.context.ValidationResult;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.format.DefaultValueFormatContext;
import org.datagen.expr.ast.format.PrettyExpressionFormatContext;
import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.parallel.ForkJoinParallelExecutor;
import org.datagen.expr.ast.parallel.ParallelExecutor;
import org.datagen.expr.parser.Parser;
import org.datagen.expr.parser.ParserResult;
import org.datagen.factory.Config;
import org.datagen.factory.ConfigBuilder;
import org.datagen.utils.DependencyOrder;
import org.datagen.utils.EmptyFunction;
import org.datagen.utils.MergeCollectors;
import org.datagen.utils.ObservableBase;

public class InterpreterImpl extends ObservableBase<Interpreter, InterpreterEvent> implements Interpreter {

	private static final class InterpreterEventImpl implements InterpreterEvent {

		private static final long serialVersionUID = 1L;

		private final String column;
		private final Value value;
		private final Value oldValue;

		protected InterpreterEventImpl(String column, Value value, Value oldValue) {
			this.column = column;
			this.value = value;
			this.oldValue = oldValue;
		}

		@Override
		public String getColumn() {
			return this.column;
		}

		@Override
		public Value getValue() {
			return this.value;
		}

		@Override
		public Value getOldValue() {
			return this.oldValue;
		}

	}

	private static final String COLUMN_NOT_FOUND_MSG = "Unknown colomn '{0}";
	private static final String COLUMN_ALREADY_EXISTS_MSG = "Column '{0}' is already defined";

	private final Map<String, ParserResult> expressions = new HashMap<>();
	private final List<String> sorted = new ArrayList<>();
	private final DateProvider dateProvider;
	private final ParallelExecutor parallelExecutor;
	private final ValueFormatContext formatContext;
	private final EvalContext context;
	private Config<InterpreterParameters> configuration;

	public InterpreterImpl() {
		this(new ConfigBuilder<InterpreterParameters>().build());
	}

	public InterpreterImpl(Config<InterpreterParameters> configuration) {
		this(configuration, new SystemDateProvider(), new DefaultValueFormatContext());
	}

	public InterpreterImpl(Config<InterpreterParameters> configuration, DateProvider dateProvider,
			ValueFormatContext formatContext) {
		this.dateProvider = dateProvider;
		this.formatContext = formatContext;
		this.configuration = configuration;

		this.parallelExecutor = configuration.isEnabled(InterpreterParameters.ENABLE_PARALLEL) ? new ForkJoinParallelExecutor()
				: null;

		this.context = new EvalContextImpl(this.dateProvider, this.formatContext,
				configuration.isEnabled(InterpreterParameters.ENABLE_PARALLEL), this.parallelExecutor, this);
	}

	@Override
	public ValidationResult registerExpression(String column, String expression) throws CircularDependencyException,
			UnresolvedDependencyException, ParsingException {
		ValidationResult validation = registerExpressionUnchecked(column, expression);

		updateDependencies();

		return validation;
	}

	@Override
	public ValidationResult registerExpression(String column, InputStream stream) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException {
		ValidationResult validation = registerExpressionUnchecked(column, stream);

		updateDependencies();

		return validation;
	}

	@Override
	public ValidationResult registerExpression(String column, Reader reader) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException {
		ValidationResult validation = registerExpressionUnchecked(column, reader);

		updateDependencies();

		return validation;
	}

	@Override
	public void unregisterExpression(String column) throws CircularDependencyException, UnresolvedDependencyException {
		if (expressions.remove(column) == null) {
			throw new IllegalArgumentException(MessageFormat.format(COLUMN_NOT_FOUND_MSG, column));
		}

		updateDependencies();
	}

	@Override
	public Map<String, Value> eval() {
		return sorted.stream().collect(Collectors.toMap(Function.<String> identity(), x -> {
			Value value = expressions.get(x).getRoot().eval(context);
			setField(x, value, true);
			return value;
		}));
	}

	@Override
	public Map<String, String> evalToString() {
		return sorted.stream().collect(Collectors.toMap(Function.<String> identity(), x -> {
			Value value = expressions.get(x).getRoot().eval(context);
			setField(x, value, true);
			return value.toValueString(formatContext);
		}));
	}

	@Override
	public Value eval(String column) {
		ParserResult ast = expressions.get(column);

		if (ast == null) {
			throw new IllegalArgumentException(MessageFormat.format(COLUMN_NOT_FOUND_MSG, column));
		}

		Value value = ast.getRoot().eval(context);
		setField(column, value, true);

		return value;
	}

	private void setField(String column, Value value, boolean notify) {
		Value old = context.setField(column, value);

		if (notify && super.isObserved()) {
			super.notify(new InterpreterEventImpl(column, value, old));
		}
	}

	@Override
	public String get(String column) {
		ParserResult ast = expressions.get(column);

		if (ast == null) {
			throw new IllegalArgumentException(MessageFormat.format(COLUMN_NOT_FOUND_MSG, column));
		}

		StringBuilder builder = new StringBuilder();

		return ast.getRoot().toString(builder, new PrettyExpressionFormatContext()).toString();
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
	public void registerExpressionsString(Map<String, String> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, ParsingException {
		for (Map.Entry<String, String> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(), expression.getValue());
		}

		updateDependencies();
	}

	@Override
	public void registerExpressionsStream(Map<String, InputStream> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException {
		for (Map.Entry<String, InputStream> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(), expression.getValue());
		}

		updateDependencies();
	}

	@Override
	public void registerExpressionsReader(Map<String, Reader> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException {
		for (Map.Entry<String, Reader> expression : expressions.entrySet()) {
			registerExpressionUnchecked(expression.getKey(), expression.getValue());
		}

		updateDependencies();
	}

	private ValidationResult registerExpressionUnchecked(String column, ParserResult result) {
		if (expressions.putIfAbsent(column, result) != null) {
			throw new IllegalArgumentException(MessageFormat.format(COLUMN_ALREADY_EXISTS_MSG, column));
		}

		ValidationContext validation = new ValidationContextImpl();
		AstWalker.walk(result.getRoot(), (n -> {
			n.validate(validation);
			return n;
		}), false);

		return validation;
	}

	private ValidationResult registerExpressionUnchecked(String column, String expression) throws ParsingException {
		ParserResult result = Parser.parse(expression, configuration, context);

		return registerExpressionUnchecked(column, result);
	}

	private ValidationResult registerExpressionUnchecked(String column, InputStream stream) throws IOException,
			ParsingException {
		ParserResult result = Parser.parse(stream, configuration, context);

		return registerExpressionUnchecked(column, result);
	}

	private ValidationResult registerExpressionUnchecked(String column, Reader reader) throws IOException,
			ParsingException {
		ParserResult result = Parser.parse(reader, configuration, context);

		return registerExpressionUnchecked(column, result);
	}

	private void updateDependencies() throws CircularDependencyException, UnresolvedDependencyException {
		Map<String, List<String>> dependencies = new HashMap<>();

		for (Map.Entry<String, ParserResult> expr : expressions.entrySet()) {
			dependencies.put(expr.getKey(), expr.getValue().getReferences().stream().map(x -> x.getReference())
					.collect(Collectors.toCollection(ArrayList::new)));
		}

		sorted.clear();
		sorted.addAll(DependencyOrder.computeOrder(dependencies));
	}

	@Override
	public void registerLibrary(String name, Map<String, String> library) throws ParsingException {
		Collection<ParsingException> embedded = new ArrayList<>();

		this.context.registerLibrary(name,
				library.entrySet().stream().collect(Collectors.toMap(Map.Entry<String, String>::getKey, x -> {
					try {
						return Parser.parse(x.getValue(), configuration, context).getRoot();
					} catch (ParsingException e) {
						embedded.add(e);
						return null;
					}
				})));

		if (!embedded.isEmpty()) {
			throw new ParsingException("Parsing of one or more library expressions failed", embedded.stream()
					.map(x -> x.getEmbedded()).collect(MergeCollectors.toCollection(ArrayList::new)));
		}
	}

	@Override
	public void unregisterLibrary(String name) {
		this.context.unregisterLibrary(name);
	}

	@Override
	public void setConfiguration(Config<InterpreterParameters> configuration) {
		this.configuration = configuration;
	}

	@Override
	public Config<InterpreterParameters> getConfiguration() {
		return this.configuration;
	}

	@Override
	public void setProperty(String property, Value value) {
		context.setProperty(property, value);
	}

	@Override
	public void setProperty(String property, EmptyFunction<Value> provider) {
		context.setProperty(property, provider);
	}

	@Override
	public void unsetProperty(String property) {
		context.unsetProperty(property);
	}

}
