package org.datagen.expr.ast;

import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import org.datagen.expr.ast.intf.ValueType;
import org.datagen.expr.ast.nodes.LiteralValue;

public final class CastMatrix {

	private interface CastOperator extends UnaryOperator<LiteralValue> {
	};

	private static final String NO_VALID_CAST_MESSAGE_PATTERN = "No suitable cast from [ {0} ] to [ {1} ]";

	private static final CastOperator IDENTITY = x -> x;

	// INTEGER REAL
	private static final CastOperator MATRIX[][] = new CastOperator[][] {
			// INTEGER
			{ IDENTITY, x -> new LiteralValue((double) x.getInteger()) },
			// REAL
			{ x -> new LiteralValue((long) x.getReal()), IDENTITY } };

	private CastMatrix() {
	}

	public static Object cast(Object from, Class<?> to) throws NoSuchElementException {
		if (from.getClass() == Double.class) {
			if (to == Double.class || to == Float.class) {
				return from;
			} else if (to == Long.class || to == Integer.class) {
				return ((Double) from).longValue();
			}
		} else if (from.getClass() == Float.class) {
			if (to == Double.class || to == Float.class) {
				return from;
			} else if (to == Long.class || to == Integer.class) {
				return ((Float) from).longValue();
			}
		} else if (from.getClass() == Long.class) {
			if (to == Double.class || to == Float.class) {
				return ((Long) from).doubleValue();
			} else if (to == Long.class || to == Integer.class) {
				return from;
			}
		} else if (from.getClass() == Integer.class) {
			if (to == Double.class || to == Float.class) {
				return ((Integer) from).doubleValue();
			} else if (to == Long.class || to == Integer.class) {
				return from;
			}
		}

		throw new NoSuchElementException(MessageFormat.format(NO_VALID_CAST_MESSAGE_PATTERN, from.getClass()
				.getSimpleName(), to.getSimpleName()));
	}

	public static LiteralValue cast(LiteralValue from, ValueType to) {
		try {
			CastOperator cast = MATRIX[from.getType().ordinal()][to.ordinal()];
			if (cast == null) {
				throw new NoSuchElementException(MessageFormat.format(NO_VALID_CAST_MESSAGE_PATTERN, from.getClass()
						.getSimpleName(), to.getTypeName()));
			}

			return cast.apply(from);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException(MessageFormat.format(NO_VALID_CAST_MESSAGE_PATTERN, from.getClass()
					.getSimpleName(), to.getTypeName()));
		}
	}
}
