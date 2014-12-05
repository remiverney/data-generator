package org.datagen.mbean;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanInfoSupport;

import org.datagen.mbean.Manageable.Invokable;

public class ManageableWrapperDynamicMBean implements CloseableDynamicMBean {

	private static final String UNSUPPORTED_ACTION_EXCEPTION_MSG_PATTERN = "Unsupported action [ {0} ] invoked on object type [ {1} ] name [ {2} ]";

	private final Manageable manageable;
	private final Map<String, Invokable> invokables;

	ManageableWrapperDynamicMBean(Manageable manageable) {
		this.manageable = manageable;
		this.invokables = manageable.getInvokable();
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
		return this.manageable.getAttribute(attribute);
	}

	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		this.manageable.setAttribute(attribute.getName(), attribute.getValue());
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		AttributeList result = new AttributeList();
		if (attributes.length == 0) {
			return result;
		}

		for (String attribute : attributes) {
			try {
				result.add(new Attribute(attribute, getAttribute(attribute)));
			} catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
				result.add(new Attribute(attribute, null));
			}
		}
		return (result);
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		for (Object obj : attributes) {
			Attribute attribute = (Attribute) obj;
			try {
				setAttribute(attribute);
			} catch (AttributeNotFoundException | InvalidAttributeValueException | MBeanException | ReflectionException e) {
			}
		}

		return attributes;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException,
			ReflectionException {
		Invokable invokable = this.invokables.get(actionName);
		if (invokable == null) {
			throw new MBeanException(new UnsupportedOperationException(MessageFormat.format(
					UNSUPPORTED_ACTION_EXCEPTION_MSG_PATTERN, actionName, this.manageable.getObjectType(),
					this.manageable.getObjectInstanceName())));
		}

		return null;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return new OpenMBeanInfoSupport(this.getClass().getName(), "", this.manageable.getAttributeInfo(),
				this.manageable.getConstructorInfo(), this.manageable.getOperationInfo(),
				this.manageable.getNotificationInfo());
	}

	@Override
	public void close() throws IOException {
		this.manageable.close();
	}

}
