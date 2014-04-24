package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class FunctionCall implements Node {

	private final String name;
	private final List<Node> parameters;

	public FunctionCall(String name, List<Node> parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public List<Node> getParameters() {
		return parameters;
	}

	@Override
	public List<Node> getChildren() {
		return parameters;
	}

	@Override
	public Value eval(EvalContext context) {
		return context.getFunctionRegistry().invokeFunction(
				this,
				name,
				parameters.stream().map(p -> p.eval(context))
						.collect(Collectors.toCollection(ArrayList::new)));
	}
}
