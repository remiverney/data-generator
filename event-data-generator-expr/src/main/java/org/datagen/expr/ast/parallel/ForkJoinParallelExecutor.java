package org.datagen.expr.ast.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.PrivateDelegateEvalContextImpl;
import org.datagen.expr.ast.exception.InterruptedParallelExecutionException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class ForkJoinParallelExecutor implements ParallelExecutor {

	private final ForkJoinPool pool;

	public ForkJoinParallelExecutor() {
		this.pool = new ForkJoinPool();
	}

	public ForkJoinParallelExecutor(int parallelism) {
		this.pool = new ForkJoinPool(parallelism);
	}

	@Override
	public void close() {
		this.pool.shutdown();
	}

	@Override
	public List<Value> eval(EvalContext context, Collection<Node> expr) {
		List<ValueComputeTask> tasks = expr.stream()
				.map(x -> new ValueComputeTask(x, new PrivateDelegateEvalContextImpl(context)))
				.collect(Collectors.toList());

		return processTasks(tasks);
	}

	@Override
	public List<Value> eval(EvalContext context, Node... expr) {
		List<ValueComputeTask> tasks = Arrays.stream(expr).map(x -> new ValueComputeTask(x, context))
				.collect(Collectors.toList());

		return processTasks(tasks);
	}

	private @Nonnull List<Value> processTasks(@Nonnull List<ValueComputeTask> tasks) {
		List<Future<Value>> results = this.pool.invokeAll(tasks);
		List<Value> values = new ArrayList<>(results.size());

		int idx = 0;
		for (Future<Value> result : results) {
			try {
				values.add(result.get());
				idx++;
			} catch (InterruptedException e) {
				throw new InterruptedParallelExecutionException(tasks.get(idx).getExpr(), e);
			} catch (ExecutionException e) {
				if (e.getCause() instanceof RuntimeException) {
					throw (RuntimeException) e.getCause();
				} else {
					throw new RuntimeException("Unexpected parallel task execution exception", e.getCause());
				}
			}
		}

		return values;
	}

	@Override
	public int getParallelism() {
		return this.pool.getParallelism();
	}

}
