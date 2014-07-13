package org.datagen.core.scheduler.impl;

import java.io.IOException;

import org.datagen.core.scheduler.JoinedTaskEvent;
import org.datagen.core.scheduler.SchedulerEvent;
import org.datagen.core.scheduler.SchedulerTask;
import org.datagen.core.scheduler.TaskException;
import org.datagen.core.scheduler.TaskExceptionEvent;
import org.datagen.core.scheduler.policy.TaskExecutionPolicy;
import org.datagen.core.scheduler.policy.TaskTerminationPolicy;
import org.datagen.utils.Observable;
import org.datagen.utils.Observer;

public class TaskThread<T extends SchedulerTask> extends Thread implements
		Observable<TaskThread<T>, SchedulerEvent> {

	private final T task;
	private final TaskExecutionPolicy scheduling;
	private final TaskTerminationPolicy termination;
	private Observer<TaskThread<T>, SchedulerEvent> observer;

	public TaskThread(T task, TaskExecutionPolicy scheduling,
			TaskTerminationPolicy termination) {
		this.task = task;
		this.scheduling = scheduling;
		this.termination = termination;
		this.observer = null;
	}

	@Override
	public void run() {
		while (!isFinished()) {
			execute();
			try {
				sleep();
			} catch (InterruptedException e) {
				if (isInterrupted()) {
					break;
				}
			}
		}

		try {
			task.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (observer != null) {
			observer.notify(this, new JoinedTaskEvent<SchedulerTask>(task));
		}
	}

	private void sleep() throws InterruptedException {
		if (scheduling != null) {
			scheduling.waitNext();
		}
	}

	private void execute() {
		try {
			task.call();
		} catch (TaskException e) {
			if (observer != null) {
				observer.notify(this, new TaskExceptionEvent<SchedulerTask>(
						task, e));
			}
		}
	}

	private boolean isFinished() {
		return (termination != null) && (termination.finished());
	}

	@Override
	public void addObserver(Observer<TaskThread<T>, SchedulerEvent> observer) {
		this.observer = observer;
	}

	@Override
	public void removeObserver(Observer<TaskThread<T>, SchedulerEvent> observer) {
		this.observer = null;
	}

	@Override
	public boolean isObserved() {
		return this.observer != null;
	}
}
