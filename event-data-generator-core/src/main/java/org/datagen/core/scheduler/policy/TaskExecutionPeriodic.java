package org.datagen.core.scheduler.policy;

import java.util.Optional;
import java.util.Random;

public class TaskExecutionPeriodic implements TaskExecutionPolicy {

	private final long period;
	private final long jitter;
	private final Optional<Random> gaussian;

	private long next;

	public TaskExecutionPeriodic(long milliseconds) {
		this(milliseconds, 0);
	}

	public TaskExecutionPeriodic(long milliseconds, long jitter) {
		this.period = milliseconds;
		this.next = System.currentTimeMillis();
		this.jitter = jitter > 0 ? jitter : 0;
		this.gaussian = (jitter > 0) ? Optional.of(new Random(System.currentTimeMillis())) : Optional.empty();
	}

	@Override
	public void waitNext() throws InterruptedException {
		long delay = this.next - System.currentTimeMillis();
		if (this.gaussian.isPresent()) {
			delay += this.gaussian.get().nextGaussian() * jitter;
		}
		if (delay > 0) {
			Thread.sleep(delay);
		}

		this.next += this.period;
	}
}
