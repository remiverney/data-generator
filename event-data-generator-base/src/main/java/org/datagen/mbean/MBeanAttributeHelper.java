package org.datagen.mbean;

import java.io.Serializable;
import java.util.Optional;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.datagen.config.Configurable;
import org.datagen.factory.BuilderParameter;
import org.datagen.factory.Config;

public final class MBeanAttributeHelper {

	public static final String[] COLUMN_NAMES = { "Name", "Type", "Value" };
	private static final String[] COLUMN_DESCRIPTIONS = { "element name", "Value type", "Value content" };
	private static final OpenType<?>[] COLUMN_TYPES = { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING };
	private static final String[] INDEX = { COLUMN_NAMES[0] };

	public static final CompositeType LINE_TYPE;
	public static final TabularType TABLE_TYPE;

	static {
		try {
			LINE_TYPE = new CompositeType("Value entry", "A value entry", COLUMN_NAMES, COLUMN_DESCRIPTIONS,
					COLUMN_TYPES);
			TABLE_TYPE = new TabularType("Value list", "A list of values", LINE_TYPE, INDEX);
		} catch (OpenDataException e) {
			throw new RuntimeException(e);
		}
	}

	private MBeanAttributeHelper() {
	}

	public static <P extends BuilderParameter<? extends Serializable>> TabularData buildConfigurationData(
			Configurable<P> configurable) {
		TabularDataSupport table = new TabularDataSupport(TABLE_TYPE);

		Optional<Config<P>> configuration = configurable.getConfiguration();
		if (configuration.isPresent()) {
			configuration.get().getKeys().forEach(p -> configuration.get().get(p).ifPresent(v -> {
				try {
					table.put(buildParameterData(p, v));
				} catch (OpenDataException e) {
				}
			}));
		}

		return table;
	}

	private static <P extends BuilderParameter<? extends Serializable>> CompositeData buildParameterData(P parameter,
			Serializable value) throws OpenDataException {
		return new CompositeDataSupport(LINE_TYPE, COLUMN_NAMES, new Object[] { parameter.getName(),
				parameter.getType().getSimpleName(), value.toString() });
	}

}
