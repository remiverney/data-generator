package org.datagen.expr.ast;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.datagen.expr.DateProvider;
import org.datagen.expr.ast.functions.FunctionRegistry;
import org.datagen.expr.ast.functions.FunctionRegistryImpl;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.nodes.LiteralValue;
import org.datagen.utils.EmptyFunction;

public class EvalContextImpl implements EvalContext {

	private static class Context<T> {
		private final Map<String, T> variables = new HashMap<>();

		private Context() {
		}

		private Context(Map<String, T> content) {
			this.variables.putAll(content);
		}

		private T get(String name) {
			return variables.get(name);
		}

		private boolean contains(String name) {
			return variables.containsKey(name);
		}

		private boolean set(String name, T value) {
			return variables.put(name, value) != null;
		}
	}

	private final Deque<Context<Value>> contexts = new ArrayDeque<>();
	private final Map<String, Value> fields = new HashMap<>();
	private final Map<String, EmptyFunction<Value>> properties = new HashMap<>();
	private final Map<String, Library> libraries = new HashMap<>();

	private final DateProvider dateProvider;
	private final ValueFormatContext formatContext;
	private final FunctionRegistry functionRegistry;

	private long sequence = 1;

	public EvalContextImpl(DateProvider dateProvider,
			ValueFormatContext formatContext) {
		this.dateProvider = dateProvider;
		this.formatContext = formatContext;
		this.functionRegistry = new FunctionRegistryImpl();

		setProperty(PROPERTY_SEQUENCE, () -> new LiteralValue(sequence));
		setProperty(PROPERTY_TIME,
				() -> new LiteralValue(dateProvider.getDate()));

		pushContext();
	}

	@Override
	public Value getProperty(String property) {
		if (properties.containsKey(property)) {
			return properties.get(property).apply();
		} else {
			return null;
		}
	}

	@Override
	public void setProperty(String property, EmptyFunction<Value> provider) {
		properties.put(property, provider);
	}

	@Override
	public void setProperty(String property, Value value) {
		properties.put(property, () -> value);
	}

	@Override
	public void unsetProperty(String property) {
		properties.remove(property);
	}

	@Override
	public Value getField(String field) {
		return fields.get(field);
	}

	@Override
	public void setField(String field, Value value) {
		fields.put(field, value);
	}

	@Override
	public void unsetField(String field) {
		fields.remove(field);
	}

	@Override
	public Value getVariable(String variable) {
		for (Context<Value> context : contexts) {
			if (context.contains(variable)) {
				return context.get(variable);
			}
		}

		return null;
	}

	@Override
	public void setVariable(String variable, Value value) {
		contexts.getFirst().set(variable, value);
	}

	@Override
	public void pushContext() {
		contexts.push(new Context<Value>());
	}

	@Override
	public void pushContext(Map<String, Value> context) {
		contexts.push(new Context<>(context));
	}

	@Override
	public void popContext() {
		contexts.pop();
	}

	@Override
	public long getSequence() {
		return sequence;
	}

	@Override
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	@Override
	public void nextSequence() {
		this.sequence++;
		this.fields.clear();
	}

	@Override
	public Date getTime() {
		return dateProvider.getDate();
	}

	@Override
	public void clear() {
		contexts.clear();
		pushContext();
		fields.clear();
	}

	@Override
	public DateProvider getDateProvider() {
		return this.dateProvider;
	}

	@Override
	public ValueFormatContext getFormatContext() {
		return formatContext;
	}

	@Override
	public FunctionRegistry getFunctionRegistry() {
		return functionRegistry;
	}

	@Override
	public Library registerLibrary(String name, Map<String, Node> entries) {
		Library library = new LibraryImpl(name, entries);
		this.libraries.put(name, library);

		return library;
	}

	@Override
	public void unregisterLibrary(String name) {
		this.libraries.remove(name);
	}

	@Override
	public Library getLibrary(String name) {
		return this.libraries.get(name);
	}

}
