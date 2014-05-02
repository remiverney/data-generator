package org.datagen.expr.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.ast.intf.Value;
import org.datagen.factory.Config;

public interface Interpreter {

	void registerExpression(String column, String expression)
			throws CircularDependencyException, UnresolvedDependencyException;

	void registerExpression(String column, InputStream stream)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException;

	void registerExpression(String column, Reader reader)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException;

	void registerExpressionsString(Map<String, String> expressions)
			throws CircularDependencyException, UnresolvedDependencyException;

	void registerExpressionsStream(Map<String, InputStream> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException;

	void registerExpressionsReader(Map<String, Reader> expressions)
			throws CircularDependencyException, UnresolvedDependencyException,
			IOException;

	void unregisterExpression(String column)
			throws CircularDependencyException, UnresolvedDependencyException;

	void registerLibrary(String name, Map<String, String> library);

	void unregisterLibrary(String name);

	void setConfiguration(Config<InterpreterParameters> configuration);

	Map<String, Value> eval();

	Value eval(String column);

	void clear();

	void setSequence(long sequence);

	void nextSequence();
}
