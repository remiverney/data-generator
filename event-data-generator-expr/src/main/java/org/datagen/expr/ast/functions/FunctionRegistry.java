package org.datagen.expr.ast.functions;

import java.util.List;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface FunctionRegistry {

	Object getFunction(@Nonnull String name);

	boolean isDeterministic(@Nonnull Node node, @Nonnull String name);

	Value invokeFunction(@Nonnull Node node, @Nonnull String name, @Nonnull List<Value> parameters);

}