package org.datagen.mbean;

import java.io.Closeable;
import java.io.IOException;

import javax.management.DynamicMBean;

public interface CloseableDynamicMBean extends DynamicMBean, Closeable {

	@Override
	void close() throws IOException;
}
