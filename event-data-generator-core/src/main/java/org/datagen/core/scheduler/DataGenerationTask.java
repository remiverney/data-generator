package org.datagen.core.scheduler;

import org.datagen.connector.OutputConnector;

public interface DataGenerationTask extends SchedulerTask {

	OutputConnector<?, ?> getOutputConnector();

}
