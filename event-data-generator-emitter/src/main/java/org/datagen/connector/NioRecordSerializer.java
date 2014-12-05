package org.datagen.connector;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

import org.datagen.dataset.DataRecord;

public interface NioRecordSerializer {

	int serialize(WritableByteChannel channel, DataRecord record) throws IOException;
}
