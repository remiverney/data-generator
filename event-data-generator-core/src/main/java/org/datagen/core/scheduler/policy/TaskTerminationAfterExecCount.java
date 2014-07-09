package org.datagen.core.scheduler.policy;

public class TaskTerminationAfterExecCount implements TaskTerminationPolicy {
	
	private long remaining;

	public TaskTerminationAfterExecCount(long count) {
		this.remaining = count;
	}

	@Override
	public boolean finished() {
		remaining--;
		return remaining > 0;
	}

}
