package org.datagen.connector.csv.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.datagen.connector.RecordSerializer;
import org.datagen.dataset.DataRecord;

public class CsvRecordSerializer implements RecordSerializer {

	private final byte[] separator;
	private final byte[] nullValue;
	private final byte[] endline;

	public CsvRecordSerializer(String separator, String nullValue, String endline) {
		this.separator = separator.getBytes();
		this.nullValue = nullValue.getBytes();
		this.endline = endline.getBytes();
	}

	@Override
	public int serialize(OutputStream writer, DataRecord record) throws IOException {
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
					len += write(writer, nullValue);
				}
			} else {
				len += write(writer, field.toString().getBytes());
			}
		}

		len += write(writer, endline);

		return len;
	}

	private static int write(OutputStream writer, byte[] buffer) throws IOException {
		writer.write(buffer);

		return buffer.length;
	}

}
