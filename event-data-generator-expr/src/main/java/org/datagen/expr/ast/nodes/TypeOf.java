package org.datagen.expr.ast.nodes;

import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class TypeOf implements Node {

	protected final Node expr;

	public TypeOf(Node expr) {
		this.expr = expr;
	}

	@Override
	public Value eval(EvalContext context) {
		return new LiteralValue(expr.eval(context).getType().getTypeName());
	}

	@Override
	public List<Node> getChildren() {
		return Collections.<Node> singletonList(expr);
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.TYPEOF);
		builder.append('(');
		expr.toString(builder, context);
		builder.append(')');

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if (expr instanceof Value) {
			return new LiteralValue(((Value) expr).getType().getTypeName());
		}

		return Node.super.optimize(context);
	}

}
