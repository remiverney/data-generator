package org.datagen.core.scheduler;

public class JoinedTaskEvent<T extends SchedulerTask> implements SchedulerEvent {

	private static final long serialVersionUID = 1L;
	
	private final T task;

	public JoinedTaskEvent(T task) {
		this.task = task;
	}

	public T getTask() {
		return task;
	}

}
