package org.datagen.core.scheduler;

public class TaskTerminationCriteria {

	public static enum Criteria {
		DURATION,
		TIME,
		NEVER
	}

	private final Criteria criteria;
	private final long duration;
	private final long executions;

	public TaskTerminationCriteria(Criteria criteria, long duration,
			long executions) {
		this.criteria = criteria;
		this.duration = duration;
		this.executions = executions;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public long getDuration() {
		return duration;
	}

	public long getExecutions() {
		return executions;
	}

}
