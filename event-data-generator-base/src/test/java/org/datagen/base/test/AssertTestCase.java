package org.datagen.base.test;

import org.datagen.validation.Asserts;
import org.junit.Test;

public class AssertTestCase {

	@Test
	public void test() {
		Asserts.enable();
		Asserts.setThrowRuntime(false);
		Asserts.checkNotNull(() -> null);
	}
}
