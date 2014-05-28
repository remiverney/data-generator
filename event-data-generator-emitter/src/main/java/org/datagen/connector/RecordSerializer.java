package org.datagen.connector;

import java.io.IOException;
import java.io.OutputStream;

import org.datagen.dataset.DataRecord;

public interface RecordSerializer {

	int serialize(OutputStream writer, DataRecord record) throws IOException;
}
