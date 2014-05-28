package org.datagen.connector.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.datagen.connector.RecordSerializer;
import org.datagen.dataset.DataRecord;

public class CsvRecordSerializer implements RecordSerializer {

	private final byte[] separator;
	private final String nullValue;

	public CsvRecordSerializer(String separator, String nullValue) {
		this.separator = separator.getBytes();
		this.nullValue = nullValue;
	}

	@Override
	public int serialize(OutputStream writer, DataRecord record)
			throws IOException {
		int len = 0;
		boolean first = true;

		for (Serializable field : record.getValues()) {
			if (first) {
				first = false;
			} else {
				len += write(writer, separator);
			}

			if (field == null) {
				if (nullValue != null) {
					len += write(writer, nullValue.getBytes());
				}
			} else {
				len += write(writer, field.toString().getBytes());
			}
		}

		return len;
	}

	private int write(OutputStream writer, byte[] buffer) throws IOException {
		writer.write(buffer);

		return buffer.length;
	}

}
