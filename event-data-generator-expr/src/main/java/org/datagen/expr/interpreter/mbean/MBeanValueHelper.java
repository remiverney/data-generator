package org.datagen.expr.interpreter.mbean;

import java.util.Map;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.intf.Value;
import org.datagen.mbean.MBeanAttributeHelper;

public final class MBeanValueHelper {

	private MBeanValueHelper() {
	}

	public static TabularData buildValueTableData(Map<String, Value> values, ValueFormatContext context) {
		TabularDataSupport table = new TabularDataSupport(MBeanAttributeHelper.TABLE_TYPE);
		values.entrySet().stream().forEach(x -> {
			try {
				table.put(buildValueData(x.getKey(), x.getValue(), context));
			} catch (OpenDataException e) {
			}
		});

		return table;
	}

	private static CompositeData buildValueData(String name, Value value, ValueFormatContext context)
			throws OpenDataException {
		return new CompositeDataSupport(MBeanAttributeHelper.LINE_TYPE, MBeanAttributeHelper.COLUMN_NAMES,
				new Object[] { name, value.getType().getTypeName(), value.toValueString(context) });
	}

}
