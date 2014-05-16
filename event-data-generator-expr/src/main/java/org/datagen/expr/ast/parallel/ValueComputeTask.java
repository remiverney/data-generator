package org.datagen.expr.ast.parallel;

import java.util.concurrent.Callable;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class ValueComputeTask implements Callable<Value> {

	private final Node expr;
	private final EvalContext context;

	ValueComputeTask(Node expr, EvalContext context) {
		this.expr = expr;
		this.context = context;
	}

	@Override
	public Value call() {
		return this.expr.eval(context);
	}

	public Node getExpr() {
		return expr;
	}

}
