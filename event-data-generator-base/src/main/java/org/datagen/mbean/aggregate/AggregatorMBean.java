package org.datagen.mbean.aggregate;

import javax.management.ObjectName;

import org.datagen.mbean.Manageable;

public interface AggregatorMBean extends Manageable {

	ObjectName getPattern();

	void registerAggregation();

	void unregisterAggregation();
}
