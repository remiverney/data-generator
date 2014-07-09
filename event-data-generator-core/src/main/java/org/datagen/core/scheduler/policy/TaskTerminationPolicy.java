package org.datagen.core.scheduler.policy;

@FunctionalInterface
public interface TaskTerminationPolicy {

	boolean finished();

}
