package org.datagen.core.scheduler.policy;


@FunctionalInterface
public interface TaskExecutionPolicy {

	void waitNext() throws InterruptedException;
}
