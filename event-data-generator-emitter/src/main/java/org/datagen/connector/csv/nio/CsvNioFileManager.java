package org.datagen.connector.csv.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import org.datagen.connector.NioRecordSerializer;
import org.datagen.connector.csv.CsvConnectorEvent;
import org.datagen.connector.csv.CsvConnectorParameters;
import org.datagen.dataset.DataRecord;
import org.datagen.factory.Config;
import org.datagen.utils.ObservableBase;

public class CsvNioFileManager extends ObservableBase<CsvNioFileManager, CsvConnectorEvent> implements Closeable {

	private final NioRecordSerializer serializer;
	private final Config<CsvConnectorParameters> configuration;
	private final String name;
	private FileChannel channel;
	private Path file;
	private long lines;
	private long size;
	private long closeTime;
	private long sequence;

	public CsvNioFileManager(Config<CsvConnectorParameters> configuration, String name, NioRecordSerializer serializer)
			throws IOException {
		this.configuration = configuration;
		this.name = name;
		this.serializer = serializer;
		this.lines = 0L;
		this.size = 0L;
		this.closeTime = -1L;
		this.sequence = 0L;

		nextFile(Optional.empty());
	}

	public int serialize(DataRecord record) throws IOException {
		int len = this.serializer.serialize(channel, record);

		this.size += len;
		this.lines++;

		checkNextFile();

		return len;
	}

	private void checkNextFile() throws IOException {
		Optional<Long> maxLines = this.configuration.<Long> get(CsvConnectorParameters.FILE_MAX_LINES);
		if (maxLines.isPresent() && this.lines >= maxLines.get()) {
			nextFile(Optional.of(CsvNioConnectorNextFileEvent.CloseReason.LINES));
			return;
		}

		Optional<Long> maxSize = this.configuration.<Long> get(CsvConnectorParameters.FILE_MAX_SIZE);
		if (maxSize.isPresent() && this.size >= maxSize.get()) {
			nextFile(Optional.of(CsvNioConnectorNextFileEvent.CloseReason.SIZE));
			return;
		}

		if (this.closeTime > 0 && this.closeTime >= System.currentTimeMillis()) {
			nextFile(Optional.of(CsvNioConnectorNextFileEvent.CloseReason.TIME));
			return;
		}
	}

	private void nextFile(Optional<CsvNioConnectorNextFileEvent.CloseReason> reason) throws IOException {
		close();

		if (reason.isPresent() && super.isObserved()) {
			super.notify(new CsvNioConnectorNextFileEvent() {
				private static final long serialVersionUID = 1L;

				@Override
				public CloseReason getCloseReason() {
					return reason.get();
				}

				@Override
				public Path getClosedFile() {
					return file;
				}
			});
		}

		this.sequence++;
		this.file = buildNextFilePath();

		this.channel = FileChannel.open(this.file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	private Path buildNextFilePath() {
		String file = MessageFormat.format(this.configuration.<String> get(CsvConnectorParameters.FILE_PATTERN).get(),
				name, this.sequence, new Date());
		return Paths.get(this.configuration.<String> get(CsvConnectorParameters.FILE_DIRECTORY).get(), file);
	}

	@Override
	public void close() throws IOException {
		if (channel != null) {
			channel.close();
			channel = null;
		}
	}

}
