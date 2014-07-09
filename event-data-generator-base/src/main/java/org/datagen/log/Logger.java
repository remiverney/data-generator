package org.datagen.log;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Priority;

public class Logger extends org.apache.log4j.Logger {

	private static final LoggerResolver<org.apache.log4j.Logger> LOGGER_RESOLVER = LogManager::getLogger;
	private static final LoggerFactory DEFAULT_FACTORY = new LoggerFactory(null);

	@FunctionalInterface
	public interface Log<T> {
		T log();
	}

	protected Logger(String name) {
		super(name);
	}

	public static Logger getLogger(String name) {
		return (Logger) LOGGER_RESOLVER.resolve(name, DEFAULT_FACTORY);
	}

	public static Logger getLogger(@SuppressWarnings("rawtypes") Class clazz) {
		return getLogger(clazz.getName());
	}

	public void debug(Log<?> message) {
		if (isDebugEnabled()) {
			super.debug(message.log());
		}
	}

	public void debug(Log<?> message, Throwable t) {
		if (isDebugEnabled()) {
			super.debug(message.log(), t);
		}
	}

	public void error(Log<?> message) {
		if (isEnabledFor(Level.ERROR)) {
			super.error(message.log());
		}
	}

	public void error(Log<?> message, Throwable t) {
		if (isEnabledFor(Level.ERROR)) {
			super.error(message.log(), t);
		}
	}

	public void fatal(Log<?> message) {
		if (isEnabledFor(Level.FATAL)) {
			super.fatal(message.log());
		}
	}

	public void fatal(Log<?> message, Throwable t) {
		if (isEnabledFor(Level.FATAL)) {
			super.fatal(message.log(), t);
		}
	}

	public void info(Log<?> message) {
		if (isInfoEnabled()) {
			super.info(message.log());
		}
	}

	public void info(Log<?> message, Throwable t) {
		if (isInfoEnabled()) {
			super.info(message.log(), t);
		}
	}

	public void log(Priority priority, Log<?> message, Throwable t) {
		if (isEnabledFor(priority)) {
			super.log(priority, message.log(), t);
		}
	}

	public void log(Priority priority, Log<?> message) {
		if (isEnabledFor(priority)) {
			super.log(priority, message.log());
		}
	}

	public void warn(Log<?> message) {
		if (isEnabledFor(Level.WARN)) {
			super.warn(message.log());
		}
	}

	public void warn(Log<?> message, Throwable t) {
		if (isEnabledFor(Level.WARN)) {
			super.warn(message.log(), t);
		}
	}

	public void trace(Log<?> message) {
		super.trace(message.log());
	}

	public void trace(Log<?> message, Throwable t) {
		super.trace(message.log(), t);
	}

}
