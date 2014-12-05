package org.datagen.core.scheduler;

import java.util.Optional;

import org.datagen.core.scheduler.policy.TaskExecutionPolicy;
import org.datagen.core.scheduler.policy.TaskTerminationPolicy;
import org.datagen.utils.Observable;

public interface Scheduler<T extends SchedulerTask> extends Runnable, Observable<Scheduler<T>, SchedulerEvent> {

	void schedule(T task, Optional<TaskExecutionPolicy> scheduling);

	void schedule(T task, Optional<TaskExecutionPolicy> scheduling, Optional<TaskTerminationPolicy> termination);

	void unshedule(T task) throws InterruptedException;

	void join() throws InterruptedException;

	void joint(T task) throws InterruptedException;

}
