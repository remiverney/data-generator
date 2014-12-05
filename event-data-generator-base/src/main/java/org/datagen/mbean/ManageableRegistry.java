package org.datagen.mbean;

import java.io.Closeable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

public interface ManageableRegistry extends Closeable {

	void register(Manageable manageable) throws MalformedObjectNameException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException;

	void unregister(Manageable manageable) throws MalformedObjectNameException, MBeanRegistrationException,
			InstanceNotFoundException;
}
