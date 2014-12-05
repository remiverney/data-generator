package org.datagen.mbean.aggregate;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AggregationDescriptor<A, T, U> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FunctionalInterface
	public interface MappedCollector<A, T, U> extends Function<Function<A, T>, Collector<A, ?, U>> {

	}

	@FunctionalInterface
	public interface LongCollector<A, U> extends Function<ToLongFunction<A>, Collector<A, ?, U>> {

	}

	@FunctionalInterface
	public interface DoubleCollector<A, U> extends Function<ToDoubleFunction<A>, Collector<A, ?, U>> {

	}

	public static LongCollector<?, Long> SUM = Collectors::summingLong;
	// public static LongCollector<?, Long> SUM = t ->
	// Collectors.summingLong(t);
	//
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// public enum Operation {
	// COUNT(Collectors.counting()),
	// SUM(t -> Collectors.summingLong((ToLongFunction) t)),
	// SUMF(t -> Collectors.summingDouble((ToDoubleFunction) t)),
	// AVG(t -> Collectors.averagingLong((ToLongFunction) t)),
	// AVGF(t -> Collectors.averagingDouble((ToDoubleFunction) t)),
	// MIN(t -> Collector.of(() -> new long[] { Long.MAX_VALUE }, (a, b) -> {
	// a[0] = Long.min(a[0], ((ToLongFunction) t).applyAsLong(b));
	// }, (a, b) -> {
	// a[0] = Long.min(a[0], b[0]);
	// return a;
	// }, a -> a[0])),
	// MINF(t -> Collector.of(() -> new double[] { Double.MAX_VALUE }, (a, b) ->
	// {
	// a[0] = Double.min(a[0], ((ToDoubleFunction) t).applyAsDouble(b));
	// }, (a, b) -> {
	// a[0] = Double.min(a[0], b[0]);
	// return a;
	// }, a -> a[0])),
	// MAX(t -> Collector.of(() -> new long[] { Long.MIN_VALUE }, (a, b) -> {
	// a[0] = Long.max(a[0], ((ToLongFunction) t).applyAsLong(b));
	// }, (a, b) -> {
	// a[0] = Long.max(a[0], b[0]);
	// return a;
	// }, a -> a[0])),
	// MAXF(t -> Collector.of(() -> new double[] { Double.MIN_VALUE }, (a, b) ->
	// {
	// a[0] = Double.max(a[0], ((ToDoubleFunction) t).applyAsDouble(b));
	// }, (a, b) -> {
	// a[0] = Double.max(a[0], b[0]);
	// return a;
	// }, a -> a[0])),
	// SUMMARY(t -> Collectors.summarizingLong((ToLongFunction) t)),
	// SUMMARYF(t -> Collectors.summarizingDouble((ToDoubleFunction) t)),
	// USER;
	//
	// private final MappedCollector<?, ?, ?> aggregator;
	//
	// private Operation() {
	// this.aggregator = null;
	// }
	//
	// private <T, A, U> Operation(Collector<T, A, U> aggregator) {
	// this.aggregator = (Function<A, T> t) -> (Collector<T, A, Object>)
	// aggregator;
	// }
	//
	// private Operation(MappedCollector<?, ?, ?> aggregator) {
	// this.aggregator = aggregator;
	// }
	//
	// private <C, T, U> Operation(Supplier<C> supplier, BiConsumer<C, T>
	// accumulator, BinaryOperator<C> combiner,
	// Function<C, U> finisher) {
	// this(Collector.of(supplier, accumulator, combiner, finisher));
	// }
	//
	// private MappedCollector<?, ?, ?> getAggregator() {
	// return this.aggregator;
	// }
	// }
	//
	// private static final long serialVersionUID = 1L;
	//
	// private final Collector<T, ?, U> aggregator;
	// private final Optional<Function<A, T>> extractor;
	//
	// public AggregationDescriptor(Operation operation) {
	// this(Optional.empty(), operation);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public AggregationDescriptor(Function<A, T> extractor, Operation
	// operation) {
	// this(Optional.of(extractor), ((MappedCollector<A, T, U>)
	// operation.getAggregator()).apply(extractor));
	// }
	//
	// @SuppressWarnings("unchecked")
	// public AggregationDescriptor(Optional<Function<A, T>> extractor,
	// Operation operation) {
	// this(extractor, ((MappedCollector<A, T, U>)
	// operation.getAggregator()).apply(extractor.orElse(t -> (T) t)));
	// }
	//
	// public AggregationDescriptor(Optional<Function<A, T>> extractor,
	// Collector<T, ?, U> aggregator) {
	// this.aggregator = aggregator;
	// this.extractor = extractor;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public U aggregate(Stream<A> stream) {
	// return (this.extractor.orElse((Function<A, T>) Function.<T> identity())
	// == Function.identity() ? (Stream<T>) stream
	// : stream.map(this.extractor.get())).collect(this.aggregator);
	// }
}
