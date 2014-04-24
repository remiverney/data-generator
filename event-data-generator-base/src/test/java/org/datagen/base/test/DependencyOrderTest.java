package org.datagen.base.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagen.exception.CircularDependencyException;
import org.datagen.exception.UnresolvedDependencyException;
import org.datagen.utils.DependencyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DependencyOrderTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDependency() throws CircularDependencyException,
			UnresolvedDependencyException {
		Map<String, List<String>> dependencies = new HashMap<>();
		List<String> sorted = DependencyOrder.computeOrder(dependencies);

		System.out.println("empty result=" + Arrays.toString(sorted.toArray()));

		dependencies.put("entry1", Arrays.asList("entry2"));
		dependencies.put("entry2", Arrays.asList("entry5"));
		dependencies.put("entry3", Arrays.asList("entry4"));
		dependencies.put("entry4", Arrays.asList("entry2"));
		dependencies.put("entry5", Arrays.asList("entry3"));
		dependencies.put("entry6", Arrays.asList());
		sorted = DependencyOrder.computeOrder(dependencies);

		System.out.println("2 single results="
				+ Arrays.toString(sorted.toArray()));
	}

}
