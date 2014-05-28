package org.datagen.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class MergeCollectors {

	private static final Set<Collector.Characteristics> CH_ID = Collections
			.unmodifiableSet(EnumSet
					.of(Collector.Characteristics.IDENTITY_FINISH));

	private MergeCollectors() {
	}

	public static <T, C extends Collection<T>> Collector<C, ?, C> toCollection(
			Supplier<C> collectionFactory) {
		return new CollectorImpl<>(collectionFactory, Collection<T>::addAll, (
				r1, r2) -> {
			r1.addAll(r2);
			return r1;
		}, CH_ID);
	}

	public static <T> Collector<List<T>, ?, List<T>> toList() {
		return new CollectorImpl<List<T>, List<T>, List<T>>(ArrayList<T>::new,
				List::addAll, (left, right) -> {
					left.addAll(right);
					return left;
				}, CH_ID);
	}
}
