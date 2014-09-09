package org.datagen.expr.ast.context;

import java.util.Date;
import java.util.Map;

import org.datagen.expr.DateProvider;
import org.datagen.expr.ast.Library;
import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.functions.FunctionRegistry;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.parallel.ParallelExecutor;
import org.datagen.expr.interpreter.Interpreter;
import org.datagen.utils.EmptyFunction;

public interface EvalContext {

	String PROPERTY_SEQUENCE = "seq";

	String PROPERTY_TIME = "time";

	String PROPERTY_STARTTIME = "starttime";

	Value getProperty(String property);

	void setProperty(String property, Value value);

	void setProperty(String property, EmptyFunction<Value> provider);

	void unsetProperty(String property);

	Value getField(String field);

	Value setField(String field, Value value);

	Value unsetField(String field);

	Value getVariable(String variable);

	void setVariable(String variable, Value value);

	void pushContext();

	void pushContext(Map<String, Value> context);

	void popContext();

	long getSequence();

	void setSequence(long sequence);

	void nextSequence();

	Date getTime();

	void clear();

	DateProvider getDateProvider();

	ValueFormatContext getFormatContext();

	FunctionRegistry getFunctionRegistry();

	Library registerLibrary(String name, Map<String, Node> entries);

	void unregisterLibrary(String name);

	Library getLibrary(String name);

	boolean isParallelizable();

	ParallelExecutor getParallelExecutor();

	Interpreter getInterpreter();
}
