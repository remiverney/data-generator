package org.datagen.connector.csv.io;

import java.io.File;

import org.datagen.connector.csv.CsvConnectorEvent;

public interface CsvConnectorNextFileEvent extends CsvConnectorEvent {

	enum CloseReason {
		SIZE,
		TIME,
		LINES
	}

	CloseReason getCloseReason();

	File getClosedFile();
}
