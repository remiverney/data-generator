package org.datagen.expr.ast.intf;

import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.derivative.DerivationContext;
import org.datagen.expr.ast.exception.NonDerivableExpressionException;
import org.datagen.expr.ast.format.ExpressionFormatContext;

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

	default public Node optimize(EvalContext context) {
		return this;
	}

	default public void validate(ValidationContext context) {
	}

	default public Node derivative(DerivationContext context) {
		throw new NonDerivableExpressionException(this);
	}

	StringBuilder toString(StringBuilder builder, ExpressionFormatContext context);
}
