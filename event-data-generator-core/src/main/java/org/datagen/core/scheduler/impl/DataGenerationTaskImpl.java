package org.datagen.core.scheduler.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.datagen.connector.OutputConnector;
import org.datagen.core.scheduler.DataGenerationTask;
import org.datagen.core.scheduler.TaskException;
import org.datagen.core.scheduler.TaskTerminationCriteria;
import org.datagen.dataset.DataDefinition;
import org.datagen.dataset.DataRecord;
import org.datagen.expr.interpreter.Interpreter;

public class DataGenerationTaskImpl implements DataGenerationTask {

	private final Interpreter interpreter;
	private final OutputConnector<?, ?> connector;
	private final DataDefinition definition;

	public DataGenerationTaskImpl(TaskTerminationCriteria termination,
			long period, Interpreter interpreter,
			OutputConnector<?, ?> connector, DataDefinition definition) {
		this.interpreter = interpreter;
		this.connector = connector;
		this.definition = definition;
	}

	@Override
	public void close() throws IOException {
		this.interpreter.clear();
		this.connector.close();
	}

	@Override
	public Void call() throws TaskException {
		Map<String, String> values = interpreter.evalToString();

		DataRecord record = new DataRecord() {
			private static final long serialVersionUID = 1L;

			@Override
			public DataDefinition getDefinition() {
				return DataGenerationTaskImpl.this.definition;
			}

			@Override
			public Serializable[] getValues() {
				return Arrays
						.stream(DataGenerationTaskImpl.this.definition
								.getFields()).map(x -> values.get(x.getName()))
						.toArray(Serializable[]::new);
			}
		};

		try {
			connector.emit(record);
		} catch (IOException e) {
			throw new TaskException(
					"Failed to emit data record to output connector", e);
		}

		return null;
	}

	@Override
	public OutputConnector<?, ?> getOutputConnector() {
		return connector;
	}

}
