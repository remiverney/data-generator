package org.datagen.expr.ast.intf;


public enum ValueType {

	INTEGER("integer"),
	REAL("real"),
	STRING("string"),
	DATE_TIME("date"),
	BOOLEAN("boolean"),
	LAMBDA("lambda"),
	ARRAY("array");

	private final String typeName;

	private ValueType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return this.typeName;
	}
}
