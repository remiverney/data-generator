package org.datagen.expr.ast.functions;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.datagen.utils.TriFunction;

public final class StringFunctions {

	private StringFunctions() {
	}

	public static final Function<String, Integer> strlen = String::length;

	public static final TriFunction<String, Long, Long, String> substring = (t,
			u, v) -> t.substring(u.intValue(), v.intValue());

	public static final BiFunction<String, String, String[]> split = String::split;
}
