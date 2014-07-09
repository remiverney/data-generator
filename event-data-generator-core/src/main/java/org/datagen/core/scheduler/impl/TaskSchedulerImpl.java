package org.datagen.core.scheduler.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.datagen.core.scheduler.Scheduler;
import org.datagen.core.scheduler.SchedulerEvent;
import org.datagen.core.scheduler.SchedulerTask;
import org.datagen.core.scheduler.policy.TaskExecutionPolicy;
import org.datagen.core.scheduler.policy.TaskTerminationPolicy;
import org.datagen.utils.Observable;
import org.datagen.utils.ObservableBase;
import org.datagen.utils.Observer;

public class TaskSchedulerImpl<T extends SchedulerTask> extends
		ObservableBase<Scheduler<T>, SchedulerEvent> implements Scheduler<T>,
		Observer<TaskThread<SchedulerTask>, SchedulerEvent> {

	private final Map<SchedulerTask, TaskThread<SchedulerTask>> threads = new ConcurrentHashMap<>();

	public TaskSchedulerImpl() {
	}

	@Override
	public void run() {
	}

	@Override
	public void schedule(T task, TaskExecutionPolicy scheduling) {
		schedule(task, scheduling, null);
	}

	@Override
	public void schedule(T task, TaskExecutionPolicy scheduling,
			TaskTerminationPolicy termination) {
		TaskThread<SchedulerTask> thread = new TaskThread<SchedulerTask>(task,
				scheduling, termination);

		thread.addObserver(this);

		threads.put(task, thread);
	}

	@Override
	public void unshedule(T task) throws InterruptedException {
		TaskThread<SchedulerTask> thread = threads.get(task);
		if (thread != null && !thread.isInterrupted()) {
			thread.interrupt();
			thread.join();
		}
	}

	@Override
	public void join() throws InterruptedException {
		for (TaskThread<SchedulerTask> thread : threads.values()) {
			thread.join();
		}
	}

	@Override
	public void joint(T task) throws InterruptedException {
		TaskThread<SchedulerTask> thread = threads.get(task);
		if (thread != null) {
			thread.join();
		}
	}

	@Override
	public void notify(
			Observable<TaskThread<SchedulerTask>, SchedulerEvent> observable,
			SchedulerEvent event) {
		super.notify(event);
	}

}
