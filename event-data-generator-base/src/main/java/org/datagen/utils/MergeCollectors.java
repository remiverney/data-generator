package org.datagen.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class MergeCollectors {

	private MergeCollectors() {
	}

	public static <T, C extends Collection<T>> Collector<C, ?, C> toCollection(
			Supplier<C> collectionFactory) {
		return Collector.<C, C> of(collectionFactory, Collection<T>::addAll, (
				r1, r2) -> {
			r1.addAll(r2);
			return r1;
		});
	}

	public static <T> Collector<List<T>, ?, List<T>> toList() {
		return Collector.<List<T>, List<T>> of(ArrayList<T>::new,
				List<T>::addAll, (left, right) -> {
					left.addAll(right);
					return left;
				});
	}
}
