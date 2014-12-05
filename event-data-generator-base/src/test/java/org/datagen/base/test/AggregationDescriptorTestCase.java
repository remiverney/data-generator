package org.datagen.base.test;

import java.util.Arrays;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import org.datagen.mbean.aggregate.AggregationDescriptorFactory;
import org.junit.Test;

public class AggregationDescriptorTestCase {

	@Test
	public void testSum() {
		ToLongFunction<String> extract = t -> t.length();
		Collector<String, ?, Long> descriptor = AggregationDescriptorFactory.buildSum(extract);

		String strings[] = { "some", "strings", "to", "check" };

		long total = Arrays.stream(strings).collect(descriptor);
		System.out.println(total);
	}
}
