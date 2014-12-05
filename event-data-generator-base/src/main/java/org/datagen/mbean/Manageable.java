package org.datagen.mbean;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;

public interface Manageable extends Closeable {

	String ATTR_CONFIGURATION = "config";

	@FunctionalInterface
	interface Invokable {
		Object invoke(Object[] params);
	}

	default OpenMBeanAttributeInfoSupport[] getAttributeInfo() {
		return new OpenMBeanAttributeInfoSupport[0];
	}

	default OpenMBeanConstructorInfoSupport[] getConstructorInfo() {
		return new OpenMBeanConstructorInfoSupport[0];
	}

	default OpenMBeanOperationInfoSupport[] getOperationInfo() {
		return new OpenMBeanOperationInfoSupport[0];
	}

	default MBeanNotificationInfo[] getNotificationInfo() {
		return new MBeanNotificationInfo[0];
	}

	default String getObjectInstanceName() {
		return String.valueOf(hashCode());
	}

	default String getObjectType() {
		return getClass().getSimpleName();
	}

	default Object getAttribute(String name) throws AttributeNotFoundException {
		throw new AttributeNotFoundException(name);
	}

	@SuppressWarnings("unused")
	default void setAttribute(String attribute, Object value) throws AttributeNotFoundException {
	}

	default Map<String, Invokable> getInvokable() {
		return Collections.emptyMap();
	}

	@Override
	default void close() throws IOException {
	}
}
