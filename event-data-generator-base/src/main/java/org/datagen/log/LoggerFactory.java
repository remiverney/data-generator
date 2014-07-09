package org.datagen.log;

public class LoggerFactory implements org.apache.log4j.spi.LoggerFactory {

	@SuppressWarnings("unused")
	private final org.apache.log4j.spi.LoggerFactory delegate;

	public LoggerFactory() {
		this(null);
	}

	public LoggerFactory(org.apache.log4j.spi.LoggerFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public Logger makeNewLoggerInstance(String name) {
		return new Logger(name);
	}

}
