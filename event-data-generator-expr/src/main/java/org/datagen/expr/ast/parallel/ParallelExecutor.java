package org.datagen.expr.ast.parallel;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface ParallelExecutor extends Closeable {

	@Nonnull
	List<Value> eval(@Nonnull EvalContext context, @Nonnull Collection<Node> expr);

	@Nonnull
	List<Value> eval(@Nonnull EvalContext context, Node... expr);

	int getParallelism();

}
