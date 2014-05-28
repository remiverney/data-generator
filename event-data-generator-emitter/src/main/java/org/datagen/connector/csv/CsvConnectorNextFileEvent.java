package org.datagen.connector.csv;

import java.io.File;

public interface CsvConnectorNextFileEvent extends CsvConnectorEvent {

	enum CloseReason {
		SIZE,
		TIME,
		LINES
	}

	CloseReason getCloseReason();

	File getClosedFile();
}
