package org.datagen.validation;
import org.datagen.log.Logger;

public class Asserts {

	private static final String CHECK_FAIL_NULL = "null value expected";
	private static final String CHECK_FAIL_NOT_NULL = "non null value expected";
	private static final String CHECK_FAIL_TRUE = "expression expected to be true";
	private static final String CHECK_FAIL_FALSE = "expression expected to be false";

	private static final String ASSERT_LOGGER_NAME = "ASSERT";

	private static final Logger TRACER = Logger.getLogger(ASSERT_LOGGER_NAME);

	private static boolean enabled = false;
	private static boolean runtime = false;

	@FunctionalInterface
	public static interface Predicate {
		boolean check();
	}

	@FunctionalInterface
	public static interface RefChecker {
		Object check();
	}

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

	public static void set(boolean enabled) {
		Asserts.enabled = enabled;
	}

	public static void setThrowRuntime(boolean runtime) {
		Asserts.runtime = runtime;
	}

	private Asserts() {
	}

	public static void checkNotNull(RefChecker predicate) {
		checkNotNull(predicate, CHECK_FAIL_NOT_NULL);
	}

	public static void checkNotNull(RefChecker predicate, String message) {
		if (enabled) {
			if (predicate.check() == null) {
				checkFail(message);
			}
		}
	}

	public static void checkNull(RefChecker predicate) {
		checkNull(predicate, CHECK_FAIL_NULL);
	}

	public static void checkNull(RefChecker predicate, String message) {
		if (enabled) {
			if (predicate.check() != null) {
				checkFail(message);
			}
		}
	}

	public static void validate(Predicate predicate) {
		validate(predicate, CHECK_FAIL_TRUE);
	}

	public static void validate(Predicate predicate, String message) {
		if (enabled) {
			if (!predicate.check()) {
				checkFail(message);
			}
		}
	}

	public static void validateFalse(Predicate predicate) {
		validateFalse(predicate, CHECK_FAIL_FALSE);
	}

	public static void validateFalse(Predicate predicate, String message) {
		if (enabled) {
			if (predicate.check()) {
				checkFail(message);
			}
		}
	}

	private static void checkFail(String message) {
		TRACER.fatal(message);
		if (runtime) {
			throw new RuntimeException(message);
		}
	}

}
