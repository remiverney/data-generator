package org.datagen.expr.ast;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface Lambda {

	int getArity();

	@Nonnull
	Node getBody();

	@Nonnull
	Node getDerivative(@Nonnull String variable, EvalContext context);

	@Nonnull
	Value eval(EvalContext context, Value... parameters);

	@Nonnull
	Value eval(EvalContext context, @Nonnull Collection<Value> parameters);
}
