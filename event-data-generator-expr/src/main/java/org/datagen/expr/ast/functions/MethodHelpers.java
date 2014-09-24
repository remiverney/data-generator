package org.datagen.expr.ast.functions;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.datagen.expr.ast.nodes.LiteralValue;

public final class MethodHelpers {

	private static final Lookup LOOKUP = MethodHandles.publicLookup();

	private static final Map<Class<?>, MethodHandle> CONSTRUCTORS = new HashMap<>();

	static {
		registerConstructor(LiteralValue.class, String.class, new Class<?>[] {});
		registerConstructor(LiteralValue.class, Date.class, new Class<?>[] {});
		registerConstructor(LiteralValue.class, boolean.class, new Class<?>[] { Boolean.class });
		registerConstructor(LiteralValue.class, long.class, new Class<?>[] { byte.class, short.class, int.class,
				Byte.class, Short.class, Integer.class, Long.class });
		registerConstructor(LiteralValue.class, double.class, new Class<?>[] { float.class, double.class, Float.class,
				Double.class });
	}

	private MethodHelpers() {
	}

	private static void registerConstructor(Class<?> type, Class<?> parameter, Class<?>... classes) {
		try {
			MethodHandle handle = LOOKUP.findConstructor(type, MethodType.methodType(void.class, parameter));
			CONSTRUCTORS.put(parameter, handle);

			for (Class<?> clazz : classes) {
				CONSTRUCTORS.put(clazz, handle);
			}
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException("Failed to register literal value constructor", e);
		}
	}

	public static LiteralValue toLiteralValue(Object value) {
		MethodHandle handle = CONSTRUCTORS.get(value.getClass());
		if (handle == null) {
			return null;
		}

		try {
			return (LiteralValue) handle.invoke(value);
		} catch (Throwable e) {
			throw new RuntimeException(MessageFormat.format(
					"Unexpected exception when building literal value from object of class [ {0} ]", value.getClass()));
		}
	}

}
