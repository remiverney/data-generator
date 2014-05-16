package org.datagen.expr.ast.context;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Map;

import org.datagen.expr.DateProvider;
import org.datagen.expr.ast.Library;
import org.datagen.expr.ast.ValueFormatContext;
import org.datagen.expr.ast.context.EvalContextImpl.Context;
import org.datagen.expr.ast.functions.FunctionRegistry;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.parallel.ParallelExecutor;
import org.datagen.utils.EmptyFunction;

public class PrivateDelegateEvalContextImpl implements EvalContext {

	private final EvalContext delegate;

	private final Deque<Context<Value>> contexts = new ArrayDeque<>();

	public PrivateDelegateEvalContextImpl(EvalContext delegate) {
		this.delegate = delegate;
		pushContext();
	}

	@Override
	public Value getProperty(String property) {
		return this.delegate.getProperty(property);
	}

	@Override
	public void setProperty(String property, Value value) {
		this.delegate.setProperty(property, value);
	}

	@Override
	public void setProperty(String property, EmptyFunction<Value> provider) {
		this.delegate.setProperty(property, provider);
	}

	@Override
	public void unsetProperty(String property) {
		this.delegate.unsetProperty(property);
	}

	@Override
	public Value getField(String field) {
		return this.delegate.getField(field);
	}

	@Override
	public Value setField(String field, Value value) {
		return this.delegate.setField(field, value);
	}

	@Override
	public Value unsetField(String field) {
		return this.delegate.unsetField(field);
	}

	@Override
	public Value getVariable(String variable) {
		for (Context<Value> context : contexts) {
			if (context.contains(variable)) {
				return context.get(variable);
			}
		}

		return this.delegate.getVariable(variable);
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
		return this.delegate.getSequence();
	}

	@Override
	public void setSequence(long sequence) {
		this.delegate.setSequence(sequence);
	}

	@Override
	public void nextSequence() {
		this.delegate.nextSequence();
	}

	@Override
	public Date getTime() {
		return this.delegate.getTime();
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	@Override
	public DateProvider getDateProvider() {
		return this.delegate.getDateProvider();
	}

	@Override
	public ValueFormatContext getFormatContext() {
		return this.delegate.getFormatContext();
	}

	@Override
	public FunctionRegistry getFunctionRegistry() {
		return this.delegate.getFunctionRegistry();
	}

	@Override
	public Library registerLibrary(String name, Map<String, Node> entries) {
		return this.delegate.registerLibrary(name, entries);
	}

	@Override
	public void unregisterLibrary(String name) {
		this.delegate.unregisterLibrary(name);
	}

	@Override
	public Library getLibrary(String name) {
		return this.delegate.getLibrary(name);
	}

	@Override
	public boolean isParallelizable() {
		return this.delegate.isParallelizable();
	}

	@Override
	public ParallelExecutor getParallelExecutor() {
		return this.delegate.getParallelExecutor();
	}

}
