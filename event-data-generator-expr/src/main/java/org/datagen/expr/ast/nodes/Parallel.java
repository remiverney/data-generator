package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class Parallel implements Node {

	private final ArrayDef expr;
	private final Optional<Node> reducer;

	public Parallel(ArrayDef expr) {
		this(expr, Optional.empty());
	}

	public Parallel(ArrayDef expr, Node reducer) {
		this(expr, Optional.of(reducer));
	}

	public Parallel(ArrayDef expr, Optional<Node> reducer) {
		this.expr = expr;
		this.reducer = reducer;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();

		children.add(expr);

		reducer.ifPresent(x -> children.add(x));

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		List<Value> intermediate = (context.isParallelizable() && expr.getSize() > 1) ? evalParallel(context)
				: evalSequential(context);
		if ((reducer.isPresent()) && (intermediate.size() > 1)) {
			Value reducerValue = reducer.get().eval(context);
			if (!reducerValue.isLambda()) {
				throw new IncompatibleArgumentException(this, 2, reducerValue.getType(), ValueType.LAMBDA);
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
		return context.getParallelExecutor().get().eval(context, expr.getItems());
	}

	private List<Value> evalSequential(EvalContext context) {
		return expr.eval(context).getItems().stream().map(x -> x.eval(context))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.PARALLEL);
		builder.append('(');
		expr.toString(builder, context);

		if (reducer.isPresent()) {
			builder.append(',');
			context.spacing(builder);
			reducer.get().toString(builder, context);
		}

		builder.append(')');

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if ((reducer.isPresent())
				&& ((expr.getSize() == 1) || expr.getItems().stream().allMatch(x -> x instanceof LiteralValue))) {
			return new ArrayDef(expr.getItems());
		} else {
			return this;
		}
	}
}
