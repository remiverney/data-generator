package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class CaseWhen implements Node {

	private final Node when;
	private final Node then;

	public CaseWhen(Node when, Node then) {
		super();
		this.when = when;
		this.then = then;
	}

	public Node getWhen() {
		return when;
	}

	public Node getThen() {
		return then;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(when);
		children.add(then);

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		return then.eval(context);
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.WHEN);
		context.spacing(builder);
		when.toString(builder, context);
		context.spacing(builder);
		context.formatKeyword(builder, Keywords.THEN);
		context.spacing(builder);
		then.toString(builder, context);

		return builder;
	}

	@Override
	public CaseWhen optimize(EvalContext context) {
		return this;
	}

}
