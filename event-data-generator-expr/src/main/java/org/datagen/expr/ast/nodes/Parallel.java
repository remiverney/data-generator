package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Parallel implements Node {

	private static final String PARALLEL_NAME = "parallel";

	private final ArrayDef expr;
	private final Node reducer;

	public Parallel(ArrayDef expr) {
		this(expr, null);
	}

	public Parallel(ArrayDef expr, Node reducer) {
		this.expr = expr;
		this.reducer = reducer;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();

		children.add(expr);

		if (reducer != null) {
			children.add(reducer);
		}

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		List<Value> intermediate = (context.isParallelizable() && expr
				.getSize() > 1) ? evalParallel(context)
				: evalSequential(context);
		if ((reducer != null) && (intermediate.size() > 1)) {
			Value reducerValue = reducer.eval(context);
			if (!reducerValue.isLambda()) {
				throw new IncompatibleArgumentException(this, 2,
						reducerValue.getType());
			}

			Lambda lambda = (Lambda) reducerValue;
			Iterator<Value> iterator = intermediate.iterator();
			Value reduced = iterator.next().eval(context);

			while (iterator.hasNext()) {
				reduced = lambda.eval(context, reduced, iterator.next());
			}

			return reduced;
		} else {
			return new ArrayDef(intermediate);
		}
	}

	private List<Value> evalParallel(EvalContext context) {
		return context.getParallelExecutor().eval(context, expr.getItems());
	}

	private List<Value> evalSequential(EvalContext context) {
		return expr.eval(context).getItems().stream().map(x -> x.eval(context))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		builder.append(PARALLEL_NAME);
		builder.append('(');
		expr.toString(builder, context);

		if (reducer != null) {
			builder.append(',');
			context.spacing(builder);
			reducer.toString(builder, context);
		}

		builder.append(')');

		return builder;
	}
}
