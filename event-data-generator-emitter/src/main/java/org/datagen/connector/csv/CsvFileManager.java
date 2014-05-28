package org.datagen.connector.csv;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Date;

import org.datagen.connector.RecordSerializer;
import org.datagen.dataset.DataRecord;
import org.datagen.factory.Config;
import org.datagen.utils.ObservableBase;

public class CsvFileManager extends
		ObservableBase<CsvFileManager, CsvConnectorEvent> implements Closeable {

	private final RecordSerializer serializer;
	private final Config<CsvConnectorParameters> configuration;
	private final String name;
	private final byte[] endline;
	private OutputStream stream;
	private File file;
	private long lines;
	private long size;
	private long closeTime;
	private long sequence;

	public CsvFileManager(Config<CsvConnectorParameters> configuration,
			String name, RecordSerializer serializer) throws IOException {
		this.configuration = configuration;
		this.name = name;
		this.serializer = serializer;
		this.stream = null;
		this.file = null;
		this.lines = 0L;
		this.size = 0L;
		this.closeTime = -1L;
		this.sequence = 0L;
		this.endline = configuration.<String> get(
				CsvConnectorParameters.LINE_SEPARATOR).getBytes();

		nextFile(null);
	}

	public int serialize(DataRecord record) throws IOException {
		int len = this.serializer.serialize(stream, record);
		stream.write(this.endline);

		this.size += len + this.endline.length;
		this.lines++;

		checkNextFile();

		return len;
	}

	private void checkNextFile() throws IOException {
		Long maxLines = this.configuration
				.<Long> get(CsvConnectorParameters.FILE_MAX_LINES);
		if (maxLines != null && this.lines >= maxLines) {
			nextFile(CsvConnectorNextFileEvent.CloseReason.LINES);
			return;
		}

		Long maxSize = this.configuration
				.<Long> get(CsvConnectorParameters.FILE_MAX_SIZE);
		if (maxSize != null && this.size >= maxSize) {
			nextFile(CsvConnectorNextFileEvent.CloseReason.SIZE);
			return;
		}

		if (this.closeTime > 0 && this.closeTime >= System.currentTimeMillis()) {
			nextFile(CsvConnectorNextFileEvent.CloseReason.TIME);
			return;
		}
	}

	private void nextFile(CsvConnectorNextFileEvent.CloseReason reason)
			throws IOException {
		close();

		if (reason != null && super.isObserved()) {
			super.notify(new CsvConnectorNextFileEvent() {
				private static final long serialVersionUID = 1L;

				@Override
				public CloseReason getCloseReason() {
					return reason;
				}

				@Override
				public File getClosedFile() {
					return file;
				}
			});
		}

		this.sequence++;
		this.file = buildNextFilePath();

		this.stream = new FileOutputStream(this.file);
	}

	private File buildNextFilePath() {
		String file = MessageFormat.format(this.configuration
				.<String> get(CsvConnectorParameters.FILE_PATTERN), name,
				this.sequence, new Date());
		return new File(
				this.configuration
						.<File> get(CsvConnectorParameters.FILE_DIRECTORY),
				file);
	}

	@Override
	public void close() throws IOException {
		if (stream != null) {
			stream.close();
			stream = null;
		}
	}

}
