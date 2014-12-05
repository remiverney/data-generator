package org.datagen.expr.ast.derivative;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;

public interface DerivationContext {

	@Nonnull
	String getVariable();

	@Nonnull
	Node derive(@Nonnull Node node);

	@Nonnull
	EvalContext getEvalContext();
}
