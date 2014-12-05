package org.datagen.mbean.aggregate;

import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class AggregationDescriptorFactory {

	private AggregationDescriptorFactory() {
	}

	// public static AggregationDescriptor<Object, Object, Long> buildCount() {
	// return new AggregationDescriptor<Object, Object,
	// Long>(AggregationDescriptor.Operation.COUNT);
	// }
	//
	public static <A> Collector<A, ?, Long> buildSum(ToLongFunction<A> extractor) {
		return Collectors.<A> summingLong(extractor);
		// return AggregationDescriptor.SUM.apply(extractor);
	}

	// public static <A> AggregationDescriptor<A, Double, Double>
	// buildSumf(Function<A, Double> extractor) {
	// return new AggregationDescriptor<A, Double, Double>(extractor,
	// AggregationDescriptor.Operation.SUMF);
	// }
	//
	// public static <A> AggregationDescriptor<A, Long, Long>
	// buildAvg(Function<A, Long> extractor) {
	// return new AggregationDescriptor<A, Long, Long>(extractor,
	// AggregationDescriptor.Operation.AVG);
	// }
	//
	// public static <A> AggregationDescriptor<A, Double, Double>
	// buildAvgf(Function<A, Double> extractor) {
	// return new AggregationDescriptor<A, Double, Double>(extractor,
	// AggregationDescriptor.Operation.AVGF);
	// }
	//
	// public static <A> AggregationDescriptor<A, Long, Long>
	// buildMin(Function<A, Long> extractor) {
	// return new AggregationDescriptor<A, Long, Long>(extractor,
	// AggregationDescriptor.Operation.MIN);
	// }
	//
	// public static <A> AggregationDescriptor<A, Double, Double>
	// buildMinf(Function<A, Double> extractor) {
	// return new AggregationDescriptor<A, Double, Double>(extractor,
	// AggregationDescriptor.Operation.MINF);
	// }
	//
	// public static <A> AggregationDescriptor<A, Long, Long>
	// buildMax(Function<A, Long> extractor) {
	// return new AggregationDescriptor<A, Long, Long>(extractor,
	// AggregationDescriptor.Operation.MAX);
	// }
	//
	// public static <A> AggregationDescriptor<A, Double, Double>
	// buildMaxf(Function<A, Double> extractor) {
	// return new AggregationDescriptor<A, Double, Double>(extractor,
	// AggregationDescriptor.Operation.MAXF);
	// }
	//
	// public static <A> AggregationDescriptor<A, Long, LongSummaryStatistics>
	// buildSummary(Function<A, Long> extractor) {
	// return new AggregationDescriptor<A, Long,
	// LongSummaryStatistics>(extractor,
	// AggregationDescriptor.Operation.SUMMARY);
	// }
	//
	// public static <A> AggregationDescriptor<A, Double,
	// DoubleSummaryStatistics> buildSummaryf(
	// Function<A, Double> extractor) {
	// return new AggregationDescriptor<A, Double,
	// DoubleSummaryStatistics>(extractor,
	// AggregationDescriptor.Operation.SUMMARYF);
	// }
	//
	// public static <A, T, U> AggregationDescriptor<A, T, U>
	// build(Optional<Function<A, T>> extractor,
	// Collector<T, ?, U> aggregator) {
	// return new AggregationDescriptor<A, T, U>(extractor, aggregator);
	// }
}
