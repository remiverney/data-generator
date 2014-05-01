package org.datagen.expr.ast.functions;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.datagen.utils.EmptyFunction;
import org.datagen.utils.VarArgFunction;

public final class MathFunctions {

	private static final long RANDOM_SEED = System.currentTimeMillis();

	private static class StaticRandom {
		private static final Random random = new Random(RANDOM_SEED);

		private static double nextDouble() {
			return random.nextDouble();
		}

		private static long nextLong() {
			return random.nextLong();
		}

		private static double nextGaussian() {
			return random.nextGaussian();
		}
	}

	private MathFunctions() {
	}

	public static final EmptyFunction<Double> random = StaticRandom::nextDouble;
	public static final EmptyFunction<Long> lrandom = StaticRandom::nextLong;

	public static final EmptyFunction<Double> gaussian = StaticRandom::nextGaussian;

	public static final Function<Double, Double> abs = Math::abs;
	public static final Function<Long, Long> labs = Math::abs;

	public static final Function<Double, Double> asin = Math::asin;
	public static final Function<Double, Double> atan = Math::atan;
	public static final BiFunction<Double, Double, Double> atan2 = Math::atan2;

	public static final Function<Double, Double> cbrt = Math::cbrt;
	public static final Function<Double, Double> ceil = Math::ceil;

	public static final Function<Double, Double> cos = Math::cos;
	public static final Function<Double, Double> cosh = Math::cosh;

	public static final Function<Double, Double> exp = Math::exp;

	public static final Function<Double, Double> floor = Math::floor;

	public static final BiFunction<Double, Double, Double> hypot = Math::hypot;

	public static final Function<Double, Double> log = Math::log;
	public static final Function<Double, Double> ln = Math::log;
	public static final Function<Double, Double> log10 = Math::log10;

	public static final VarArgFunction<Double, Double> max = t -> Arrays
			.asList(t).stream().reduce(Double.MIN_VALUE, Math::max);

	public static final VarArgFunction<Double, Double> min = t -> Arrays
			.asList(t).stream().reduce(Double.MAX_VALUE, Math::min);

	public static final VarArgFunction<Long, Long> lmax = t -> Arrays.asList(t)
			.stream().reduce(Long.MIN_VALUE, Math::max);

	public static final VarArgFunction<Long, Long> lmin = t -> Arrays.asList(t)
			.stream().reduce(Long.MAX_VALUE, Math::min);

	public static final Function<Double, Long> round = Math::round;
	public static final Function<Double, Double> signum = Math::signum;

	public static final Function<Double, Double> sin = Math::sin;
	public static final Function<Double, Double> sinh = Math::sinh;

	public static final Function<Double, Double> sqrt = Math::sqrt;

	public static final Function<Double, Double> tan = Math::tan;
	public static final Function<Double, Double> tanh = Math::tanh;

	public static final Function<Double, Double> deg = Math::toDegrees;
	public static final Function<Double, Double> rad = Math::toRadians;

	public static final Function<Double, Boolean> isNan = (x -> Double.isNaN(x));
	public static final Function<Double, Boolean> isInfinite = (x -> Double
			.isInfinite(x));
	public static final Function<Double, Boolean> isFinite = (x -> Double
			.isFinite(x));
}
