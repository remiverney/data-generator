package org.datagen.core.scheduler.impl;

import java.io.IOException;
import java.util.Optional;

import org.datagen.core.scheduler.JoinedTaskEvent;
import org.datagen.core.scheduler.SchedulerEvent;
import org.datagen.core.scheduler.SchedulerTask;
import org.datagen.core.scheduler.TaskException;
import org.datagen.core.scheduler.TaskExceptionEvent;
import org.datagen.core.scheduler.policy.TaskExecutionPolicy;
import org.datagen.core.scheduler.policy.TaskTerminationPolicy;
import org.datagen.utils.Observable;
import org.datagen.utils.Observer;

public class TaskThread<T extends SchedulerTask> extends Thread implements Observable<TaskThread<T>, SchedulerEvent> {

	private final T task;
	private final Optional<TaskExecutionPolicy> scheduling;
	private final Optional<TaskTerminationPolicy> termination;
	private Optional<Observer<TaskThread<T>, SchedulerEvent>> observer;

	public TaskThread(T task, Optional<TaskExecutionPolicy> scheduling, Optional<TaskTerminationPolicy> termination) {
		this.task = task;
		this.scheduling = scheduling;
		this.termination = termination;
		this.observer = Optional.empty();
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

		observer.ifPresent(x -> x.notify(this, new JoinedTaskEvent<SchedulerTask>(task)));
	}

	private void sleep() throws InterruptedException {
		if (scheduling.isPresent()) {
			scheduling.get().waitNext();
		}
	}

	private void execute() {
		try {
			task.call();
		} catch (TaskException e) {
			observer.ifPresent(x -> x.notify(this, new TaskExceptionEvent<SchedulerTask>(task, e)));
		}
	}

	private boolean isFinished() {
		return (termination.isPresent()) && (termination.get().finished());
	}

	@Override
	public void addObserver(Observer<TaskThread<T>, SchedulerEvent> observer) {
		this.observer = Optional.of(observer);
	}

	@Override
	public void removeObserver(Observer<TaskThread<T>, SchedulerEvent> observer) {
		this.observer = Optional.empty();
	}

	@Override
	public boolean isObserved() {
		return this.observer.isPresent();
	}
}
