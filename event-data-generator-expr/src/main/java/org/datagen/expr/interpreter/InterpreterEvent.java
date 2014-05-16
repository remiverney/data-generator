package org.datagen.expr.interpreter;

import org.datagen.expr.ast.intf.Value;

public interface InterpreterEvent {

	String getColumn();

	Value getValue();

	Value getOldValue();
}
