package org.datagen.expr.ast.derivative;

import java.util.HashMap;
import java.util.Map;

import org.datagen.expr.ast.intf.Node;

public class DerivationContextimpl implements DerivationContext {

	private final String variable;

	private final Map<Node, Node> cache = new HashMap<>();

	public DerivationContextimpl(String variable) {
		this.variable = variable;
	}

	@Override
	public String getVariable() {
		return variable;
	}

	@Override
	public Node derive(Node node) {
		Node cached = cache.get(node);
		if (cached != null) {
			return cached;
		}

		cached = node.derivative(this).optimize(null);
		cache.put(node, cached);

		return cached;
	}

}
