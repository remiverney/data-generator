package org.datagen.expr.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.intf.Value;
import org.datagen.factory.Config;
import org.datagen.utils.EmptyFunction;
import org.datagen.utils.Observable;

public interface Interpreter extends Observable<Interpreter, InterpreterEvent> {

	void registerExpression(String column, String expression)
			throws CircularDependencyException, UnresolvedDependencyException,
			ParsingException;

	void registerExpression(String column, InputStream stream)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException, ParsingException;

	void registerExpression(String column, Reader reader)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException, ParsingException;

	void registerExpressionsString(Map<String, String> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			ParsingException;

	void registerExpressionsStream(Map<String, InputStream> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException, ParsingException;

	void registerExpressionsReader(Map<String, Reader> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException, ParsingException;

	void unregisterExpression(String column)
			throws CircularDependencyException, UnresolvedDependencyException;

	void registerLibrary(String name, Map<String, String> library)
			throws ParsingException;

	void unregisterLibrary(String name);

	void setProperty(String property, Value value);

	void setProperty(String property, EmptyFunction<Value> provider);

	void unsetProperty(String property);

	void setConfiguration(Config<InterpreterParameters> configuration);

	Map<String, Value> eval();

	Value eval(String column);

	Map<String, String> evalToString();

	void clear();

	void setSequence(long sequence);

	void nextSequence();
}
