package org.datagen.expr.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.datagen.config.Configurable;
import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.expr.ast.context.ValidationResult;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.intf.Value;
import org.datagen.factory.Config;
import org.datagen.mbean.Manageable;
import org.datagen.utils.EmptyFunction;
import org.datagen.utils.Identifiable;
import org.datagen.utils.Observable;

public interface Interpreter extends Observable<Interpreter, InterpreterEvent>, Identifiable<String>, Manageable,
		Configurable<InterpreterParameters> {

	@Nonnull
	ValidationResult registerExpression(@Nonnull String column, @Nonnull String expression)
			throws CircularDependencyException, UnresolvedDependencyException, ParsingException;

	@Nonnull
	ValidationResult registerExpression(@Nonnull String column, @Nonnull InputStream stream)
			throws CircularDependencyException, UnresolvedDependencyException, IOException, ParsingException;

	@Nonnull
	ValidationResult registerExpression(@Nonnull String column, @Nonnull Reader reader)
			throws CircularDependencyException, UnresolvedDependencyException, IOException, ParsingException;

	void registerExpressionsString(@Nonnull Map<String, String> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, ParsingException;

	void registerExpressionsStream(@Nonnull Map<String, InputStream> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException;

	void registerExpressionsReader(@Nonnull Map<String, Reader> expressions) throws CircularDependencyException,
			UnresolvedDependencyException, IOException, ParsingException;

	void unregisterExpression(@Nonnull String column) throws CircularDependencyException, UnresolvedDependencyException;

	void registerLibrary(@Nonnull String name, @Nonnull Map<String, String> library) throws ParsingException;

	void unregisterLibrary(@Nonnull String name);

	void setProperty(@Nonnull String property, @Nonnull Value value);

	void setProperty(@Nonnull String property, @Nonnull EmptyFunction<Value> provider);

	void unsetProperty(@Nonnull String property);

	void setConfiguration(Optional<Config<InterpreterParameters>> configuration);

	@Nonnull
	Map<String, Value> eval();

	@Nonnull
	Value eval(String column);

	@Nonnull
	Map<String, String> evalToString();

	String get(@Nonnull String column);

	void clear();

	void setSequence(long sequence);

	void nextSequence();

	void setJavaRefClassLoader(ClassLoader loader);

	@Nonnull
	Optional<ClassLoader> getJavaRefClassLoader();
}
