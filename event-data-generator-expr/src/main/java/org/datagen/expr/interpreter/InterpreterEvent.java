package org.datagen.expr.interpreter;

import java.io.Serializable;

import org.datagen.expr.ast.intf.Value;

public interface InterpreterEvent extends Serializable {

	String getColumn();

	Value getValue();

	Value getOldValue();
}
