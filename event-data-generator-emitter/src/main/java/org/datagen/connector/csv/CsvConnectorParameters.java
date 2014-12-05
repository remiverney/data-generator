package org.datagen.connector.csv;

import java.io.Serializable;
import java.util.Optional;

import org.datagen.factory.BuilderParameter;

public enum CsvConnectorParameters implements BuilderParameter<Serializable> {
	FIELD_SEPARATOR(String.class, ","),
	LINE_SEPARATOR(String.class, "\n"),
	NULL_SERIALIZATION(String.class, ""),
	FILE_DIRECTORY(String.class),
	FILE_PATTERN(String.class, "csv-{0}-{1, number,000000}-{2, date,yyyy-MM-dd-HHmmss}.csv"),
	FILE_MAX_LINES(Long.class, 10L),
	FILE_MAX_SIZE(Long.class, 1024L),
	FILE_MAX_TIME(Long.class, 5L);

	private final Class<? extends Serializable> type;
	private final Optional<Serializable> defaultValue;

	private <T extends Serializable> CsvConnectorParameters(Class<T> type) {
		this.type = type;
		this.defaultValue = Optional.empty();
	}

	private <T extends Serializable> CsvConnectorParameters(Class<T> type, T defaultValue) {
		this.type = type;
		this.defaultValue = Optional.of(defaultValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Serializable> getType() {
		return (Class<Serializable>) this.type;
	}

	@Override
	public Optional<Serializable> getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public String getName() {
		return name();
	}

}
