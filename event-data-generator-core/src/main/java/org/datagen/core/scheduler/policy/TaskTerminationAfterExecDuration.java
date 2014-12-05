package org.datagen.core.scheduler.policy;

import org.datagen.utils.annotation.Immutable;

@Immutable
public class TaskTerminationAfterExecDuration implements TaskTerminationPolicy {

	private final long end;

	public TaskTerminationAfterExecDuration(long milliseconds) {
		this.end = System.currentTimeMillis() + milliseconds;
	}

	@Override
	public boolean finished() {
		return System.currentTimeMillis() >= this.end;
	}

}
