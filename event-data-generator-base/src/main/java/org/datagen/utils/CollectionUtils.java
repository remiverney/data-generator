package org.datagen.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	public static <T> int positionOf(List<T> list, Predicate<T> predicate) {
		int i = 0;
		for (T item : list) {
			if (predicate.test(item)) {
				return i;
			}

			i++;
		}

		return -1;
	}

	public static <T> int positionOf(List<T> list, T item) {
		return positionOf(list, (Predicate<T>) x -> x.equals(item));
	}

	public static <T> List<T> copyRange(List<T> list, int from, int to) {
		List<T> copy = new ArrayList<>(to - from + 1);

		for (int i = from; i < to; i++) {
			copy.add(list.get(i));
		}

		return copy;
	}

	public static <T> List<T> concat(Collection<T> list1, Collection<T> list2) {
		List<T> result = new ArrayList<>(list1);
		result.addAll(list2);

		return result;
	}

}
