package org.datagen.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;

public final class DependencyOrder {

	private DependencyOrder() {
	}

	private static void insertOrdered(Map<String, List<String>> entries, List<String> sorted, List<String> pendings,
			String entry, Optional<String> dependent) throws CircularDependencyException, UnresolvedDependencyException {

		if (sorted.contains(entry)) {
			return;
		}

		if (pendings.contains(entry)) {
			int start = CollectionUtils.positionOf(pendings, entry);

			throw new CircularDependencyException(CollectionUtils.copyRange(pendings, start, pendings.size()));
		}

		if (!entries.containsKey(entry)) {
			throw new UnresolvedDependencyException(entry, dependent);
		}

		List<String> dependencies = entries.get(entry);
		pendings.add(entry);

		if (dependencies != null) {
			for (String dep : dependencies) {
				insertOrdered(entries, sorted, pendings, dep, Optional.of(entry));
			}
		}

		pendings.remove(entry);
		sorted.add(entry);
	}

	public static List<String> computeOrder(Map<String, List<String>> dependencies) throws CircularDependencyException,
			UnresolvedDependencyException {
		System.out.println("deps: ");
		for (Map.Entry<String, List<String>> dep : dependencies.entrySet()) {
			System.out.println("   dep " + dep.getKey() + "->" + Arrays.toString(dep.getValue().toArray()));
		}

		List<String> sorted = new ArrayList<>();
		List<String> pendings = new ArrayList<>();

		for (Iterator<Map.Entry<String, List<String>>> iterator = dependencies.entrySet().iterator(); iterator
				.hasNext();) {

			insertOrdered(dependencies, sorted, pendings, iterator.next().getKey(), Optional.empty());
		}

		System.out.println("ordered: " + Arrays.toString(sorted.toArray()));

		return sorted;
	}

}
