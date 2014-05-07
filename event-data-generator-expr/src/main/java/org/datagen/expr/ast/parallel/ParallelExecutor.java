package org.datagen.expr.ast.parallel;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public interface ParallelExecutor extends Closeable {

	List<Value> eval(EvalContext context, Collection<Node> expr);

	List<Value> eval(EvalContext context, Node... expr);

	int getParallelism();

}
