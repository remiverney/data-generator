package org.datagen.expr.ast.format;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.Mapped;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class JsonValueFormatContext implements ValueFormatContext {

	private static final long serialVersionUID = 1L;

	private static final DatatypeFactory DATATYPE_FACTORY;

	static {
		try {
			DATATYPE_FACTORY = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(
					"Failed to create instance of datatype factory", e);
		}
	}

	public JsonValueFormatContext() {
	}

	@Override
	public String formatString(String value) {
		return "\"" + value + "\"";
	}

	@Override
	public String formatInteger(long value) {
		return String.valueOf(value);
	}

	@Override
	public String formatReal(double value) {
		return String.valueOf(value);
	}

	@Override
	public String formatBoolean(boolean value) {
		return String.valueOf(value);
	}

	@Override
	public String formatDate(Date value) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(value);
		return DATATYPE_FACTORY.newXMLGregorianCalendar(calendar).toXMLFormat();
	}

	@Override
	public String formatLambda(Lambda lambda) {
		throw new UnsupportedOperationException(
				"Cannot format a lambda function");
	}

	@Override
	public String formatArray(Array array) {
		return array.getAll().stream().sequential()
				.map(x -> x.eval(null).toValueString(this))
				.collect(Collectors.joining(", ", "[ ", " ]"));
	}

	@Override
	public String formatMapped(Mapped mapped) {
		return mapped
				.getAll()
				.entrySet()
				.stream()
				.sequential()
				.map(x -> stringify(x.getKey().eval(null)) + " : "
						+ x.getValue().eval(null).toValueString(this))
				.collect(Collectors.joining(", ", "{ ", " }"));
	}

	private String stringify(Value value) {
		return value.getType() == ValueType.STRING ? value.toValueString(this)
				: "\"" + value.toValueString(this) + "\"";
	}

}
