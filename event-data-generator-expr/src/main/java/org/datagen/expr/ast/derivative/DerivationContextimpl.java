package org.datagen.expr.ast.derivative;

import java.util.HashMap;
import java.util.Map;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class DerivationContextimpl implements DerivationContext {

	private final String variable;
	private final EvalContext evalContext;

	private final Map<Node, Node> cache = new HashMap<>();

	public DerivationContextimpl(String variable, EvalContext evalContext) {
		this.variable = variable;
		this.evalContext = evalContext;
	}

	@Override
	public String getVariable() {
		return variable;
	}

	@Override
	public Node derive(Node node) {
		return cache.computeIfAbsent(node, n -> n.derivative(this).optimize(evalContext));
	}

	@Override
	public EvalContext getEvalContext() {
		return this.evalContext;
	}

}
