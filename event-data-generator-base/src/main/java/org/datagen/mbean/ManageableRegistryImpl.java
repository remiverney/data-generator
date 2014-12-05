package org.datagen.mbean;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class ManageableRegistryImpl implements ManageableRegistry {

	private final String domain;
	private final MBeanServer server;
	private final Map<ObjectName, CloseableDynamicMBean> manageables = new HashMap<>();

	public ManageableRegistryImpl(String domain) {
		this(domain, ManagementFactory.getPlatformMBeanServer());
	}

	public ManageableRegistryImpl(String domain, MBeanServer server) {
		this.domain = domain;
		this.server = server;
	}

	@Override
	public void register(Manageable manageable) throws MalformedObjectNameException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException {
		ObjectName name = getManageableName(manageable);
		CloseableDynamicMBean mbean = new ManageableWrapperDynamicMBean(manageable);
		this.server.registerMBean(mbean, name);
		this.manageables.put(name, mbean);
	}

	@Override
	public void unregister(Manageable manageable) throws MalformedObjectNameException, MBeanRegistrationException,
			InstanceNotFoundException {
		ObjectName name = getManageableName(manageable);
		this.server.unregisterMBean(name);

		@SuppressWarnings("resource")
		CloseableDynamicMBean mbean = this.manageables.remove(name);
		if (mbean != null) {
			try {
				mbean.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public void close() throws IOException {
		this.manageables.entrySet().stream().forEach(x -> {
			try {
				this.server.unregisterMBean(x.getKey());
				x.getValue().close();
			} catch (MBeanRegistrationException | InstanceNotFoundException | IOException e) {
			}
		});

		this.manageables.clear();

	}

	private ObjectName getManageableName(Manageable manageable) throws MalformedObjectNameException {
		return new ObjectName(domain, manageable.getObjectType(), manageable.getObjectInstanceName());
	}

}
