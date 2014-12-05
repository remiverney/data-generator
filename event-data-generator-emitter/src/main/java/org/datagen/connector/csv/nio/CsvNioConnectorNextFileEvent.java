package org.datagen.connector.csv.nio;

import java.nio.file.Path;

import org.datagen.connector.csv.CsvConnectorEvent;

public interface CsvNioConnectorNextFileEvent extends CsvConnectorEvent {

	enum CloseReason {
		SIZE,
		TIME,
		LINES
	}

	CloseReason getCloseReason();

	Path getClosedFile();
}
