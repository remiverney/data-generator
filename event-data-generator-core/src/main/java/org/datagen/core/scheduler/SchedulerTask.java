package org.datagen.core.scheduler;

import java.io.Closeable;

import org.datagen.utils.CallableExt;

public interface SchedulerTask extends CallableExt<Void, TaskException>, Closeable {

}
