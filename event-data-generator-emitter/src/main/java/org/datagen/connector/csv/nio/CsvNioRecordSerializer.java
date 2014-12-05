package org.datagen.connector.csv.nio;

import java.io.IOException;
import java.io.Serializable;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.datagen.connector.NioRecordSerializer;
import org.datagen.dataset.DataRecord;

public class CsvNioRecordSerializer implements NioRecordSerializer {

	private static final int INITIAL_BUFFER_CAPACITY = 256;

	private final byte[] separator;
	private final byte[] nullValue;
	private final byte[] endline;
	private ByteBuffer buffer;

	public CsvNioRecordSerializer(String separator, String nullValue, String endline) {
		this.separator = separator.getBytes();
		this.nullValue = nullValue.getBytes();
		this.endline = endline.getBytes();
		this.buffer = ByteBuffer.allocate(INITIAL_BUFFER_CAPACITY);
	}

	@Override
	public int serialize(WritableByteChannel channel, DataRecord record) throws IOException {
		int len = 0;
		boolean first = true;

		for (Serializable field : record.getValues()) {
			if (first) {
				first = false;
			} else {
				buffer = write(buffer, separator);
				len += separator.length;
			}

			if (field == null) {
				if (nullValue != null) {
					buffer = write(buffer, nullValue);
					len += nullValue.length;
				}
			} else {
				byte[] data = field.toString().getBytes();
				buffer = write(buffer, data);
				len += data.length;
			}
		}

		buffer = write(buffer, endline);
		len += endline.length;

		buffer.flip();
		channel.write(buffer);
		buffer.clear();

		return len;
	}

	private static ByteBuffer write(ByteBuffer buffer, byte[] data) {
		try {
			buffer.put(data);
			return buffer;
		} catch (BufferOverflowException e) {
			ByteBuffer realloc = ByteBuffer
					.allocate(Integer.max(buffer.capacity() * 2, buffer.capacity() + data.length));
			realloc.put(buffer);
			realloc.put(data);

			return realloc;
		}
	}

}
