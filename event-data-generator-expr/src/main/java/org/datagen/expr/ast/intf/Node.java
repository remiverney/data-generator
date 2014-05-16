package org.datagen.expr.ast.intf;

import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.context.EvalContext;

public interface Node extends Composite<Node>, Visitable<Node> {

	Value eval(EvalContext context);

	default @Override public Node visit(Visitable.Visitor<Node> visitor) {
		return visitor.visit(this);
	}

	default @Override public List<Node> getChildren() {
		return Collections.<Node> emptyList();
	}

	default public int getSourceLine() {
		return 0;
	}

	default public int getSourceCol() {
		return 0;
	}

	default public Node optimize() {
		return this;
	}

	StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context);
}
