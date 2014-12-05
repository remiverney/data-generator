package org.datagen.expr.ast.intf;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.derivative.DerivationContext;
import org.datagen.expr.ast.exception.NonDerivableExpressionException;
import org.datagen.expr.ast.format.ExpressionFormatContext;

public interface Node extends Composite<Node>, Visitable<Node> {

	@Nonnull
	Value eval(EvalContext context);

	default @Nonnull @Override public Node visit(Visitable.Visitor<Node> visitor) {
		return visitor.visit(this);
	}

	default @Nonnull @Override public List<Node> getChildren() {
		return Collections.<Node> emptyList();
	}

	default public int getSourceLine() {
		return 0;
	}

	default public int getSourceCol() {
		return 0;
	}

	default public void validate(ValidationContext context) {
	}

	default public @Nonnull Node derivative(DerivationContext context) {
		throw new NonDerivableExpressionException(this);
	}

	default @Nonnull Node optimize(EvalContext context) {
		return this;
	}

	@Nonnull
	StringBuilder toString(StringBuilder builder, ExpressionFormatContext context);
}
