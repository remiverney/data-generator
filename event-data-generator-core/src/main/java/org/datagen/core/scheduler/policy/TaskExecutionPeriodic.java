package org.datagen.core.scheduler.policy;

import java.util.Random;

public class TaskExecutionPeriodic implements TaskExecutionPolicy {

	private final long period;
	private final long jitter;
	private final Random gaussian;

	private long next;

	public TaskExecutionPeriodic(long milliseconds) {
		this(milliseconds, 0);
	}

	public TaskExecutionPeriodic(long milliseconds, long jitter) {
		this.period = milliseconds;
		this.next = System.currentTimeMillis();
		this.jitter = jitter > 0 ? jitter : 0;
		this.gaussian = (jitter > 0) ? new Random(System.currentTimeMillis())
				: null;
	}

	@Override
	public void waitNext() throws InterruptedException {
		long delay = this.next - System.currentTimeMillis();
		if (jitter > 0) {
			delay += this.gaussian.nextGaussian() * jitter;
		}
		if (delay > 0) {
			synchronized (this) {
				wait(delay);
			}
		}

		this.next += this.period;
	}
}
