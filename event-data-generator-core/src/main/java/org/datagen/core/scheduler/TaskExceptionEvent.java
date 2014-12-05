package org.datagen.core.scheduler;

import org.datagen.utils.annotation.Immutable;

@Immutable
public class TaskExceptionEvent<T extends SchedulerTask> implements SchedulerEvent {

	private static final long serialVersionUID = 1L;

	private final T task;
	private final TaskException exception;

	public TaskExceptionEvent(T task, TaskException exception) {
		this.task = task;
		this.exception = exception;
	}

	public T getTask() {
		return task;
	}

	public TaskException getException() {
		return exception;
	}

}
