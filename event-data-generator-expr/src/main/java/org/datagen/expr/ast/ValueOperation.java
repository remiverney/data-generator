package org.datagen.expr.ast;

import java.util.Date;

import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.IncompatibleTypesException;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Comparison;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.expr.ast.nodes.ArithmeticOp;
import org.datagen.expr.ast.nodes.ArrayDef;
import org.datagen.expr.ast.nodes.LambdaDef;
import org.datagen.expr.ast.nodes.LiteralValue;
import org.datagen.utils.CollectionUtils;

public strictfp final class ValueOperation {

	private ValueOperation() {
	}

	public static strictfp Value add(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case ARRAY:
			switch (v2.getType()) {
			case ARRAY:
				return new ArrayDef(CollectionUtils.concat(((ArrayDef) v1).getItems(), ((ArrayDef) v2).getItems()));
			case LAMBDA:
				return new LambdaDef(((LambdaDef) v2).getParameters(), new ArithmeticOp(v1, ((LambdaDef) v2).getBody(),
						Arithmetic.ADD)).eval(context);
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
			}
		case DATE_TIME:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(new Date(((LiteralValue) v1).getDate().getTime()
						+ ((LiteralValue) v2).getInteger()));
			case LAMBDA:
				return new LambdaDef(((LambdaDef) v2).getParameters(), new ArithmeticOp(v1, ((LambdaDef) v2).getBody(),
						Arithmetic.ADD)).eval(context);
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
			}
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() + ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getInteger() + ((LiteralValue) v2).getReal());
			case STRING:
				return new LiteralValue(v1.toValueString(context.getFormatContext())
						+ v2.toValueString(context.getFormatContext()));
			case LAMBDA:
				return new LambdaDef(((LambdaDef) v2).getParameters(), new ArithmeticOp(v1, ((LambdaDef) v2).getBody(),
						Arithmetic.ADD)).eval(context);
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getReal() + ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getReal() + ((LiteralValue) v2).getReal());
			case STRING:
				return new LiteralValue(v1.toValueString(context.getFormatContext())
						+ v2.toValueString(context.getFormatContext()));
			case LAMBDA:
				return new LambdaDef(((LambdaDef) v2).getParameters(), new ArithmeticOp(v1, ((LambdaDef) v2).getBody(),
						Arithmetic.ADD)).eval(context);
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
			}
		case STRING:
			switch (v2.getType()) {
			case LAMBDA:
				return new LambdaDef(((LambdaDef) v2).getParameters(), new ArithmeticOp(v1, ((LambdaDef) v2).getBody(),
						Arithmetic.ADD)).eval(context);
			default:
				return new LiteralValue(v1.toValueString(context.getFormatContext())
						+ v2.toValueString(context.getFormatContext()));
			}
		case LAMBDA:
			switch (v2.getType()) {
			case LAMBDA:
				throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
			default:
				return new LambdaDef(((LambdaDef) v1).getParameters(), new ArithmeticOp(((LambdaDef) v1).getBody(), v2,
						Arithmetic.ADD)).eval(context);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.ADD, v1, v2);
		}
	}

	public static strictfp Value sub(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case DATE_TIME:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(new Date(((LiteralValue) v1).getDate().getTime()
						- ((LiteralValue) v2).getInteger()));
			case DATE_TIME:
				return new LiteralValue(((LiteralValue) v1).getDate().getTime()
						- ((LiteralValue) v2).getDate().getTime());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.SUB, v1, v2);
			}
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() - ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getInteger() - ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.SUB, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getReal() - ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getReal() - ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.SUB, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.SUB, v1, v2);
		}
	}

	public static strictfp Value mul(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() * ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getInteger() * ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.MUL, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getReal() * ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getReal() * ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.MUL, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.MUL, v1, v2);
		}
	}

	public static strictfp Value div(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() / ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getInteger() / ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.DIV, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getReal() / ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getReal() / ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.DIV, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.DIV, v1, v2);
		}
	}

	public static strictfp Value mod(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() % ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getInteger() % ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.MOD, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getReal() % ((LiteralValue) v2).getInteger());
			case REAL:
				return new LiteralValue(((LiteralValue) v1).getReal() % ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.MOD, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.MOD, v1, v2);
		}
	}

	public static strictfp Value neg(EvalContext context, Node operator, Value v) {
		switch (v.getType()) {
		case INTEGER:
			return new LiteralValue(-((LiteralValue) v).getInteger());
		case REAL:
			return new LiteralValue(-((LiteralValue) v).getReal());
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.NEG, v);
		}
	}

	public static LiteralValue fact(EvalContext context, Node operator, Value v) {
		ensureIntegerValue(operator, Arithmetic.FACT, v);

		long n = ((LiteralValue) v).getInteger();

		long accum = 1;

		for (; n > 1; n--) {
			accum *= n;
		}

		return new LiteralValue(accum);
	}

	public static strictfp Value pow(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(Math.pow(((LiteralValue) v1).getInteger(), ((LiteralValue) v2).getInteger()));
			case REAL:
				return new LiteralValue(Math.pow(((LiteralValue) v1).getInteger(), ((LiteralValue) v2).getReal()));
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.POW, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(Math.pow(((LiteralValue) v1).getReal(), ((LiteralValue) v2).getInteger()));
			case REAL:
				return new LiteralValue(Math.pow(((LiteralValue) v1).getReal(), ((LiteralValue) v2).getReal()));
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.POW, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.POW, v1, v2);
		}
	}

	public static Value shl(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() << ((LiteralValue) v2).getInteger());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.SHL, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.SHL, v1, v2);
		}
	}

	public static Value shr(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return new LiteralValue(((LiteralValue) v1).getInteger() >> ((LiteralValue) v2).getInteger());
			default:
				throw new IncompatibleTypesException(operator, Arithmetic.SHR, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Arithmetic.SHR, v1, v2);
		}
	}

	public static LiteralValue and(EvalContext context, Node operator, Value v1, Value v2) {
		ensureBooleanValue(operator, Logic.AND, v1);
		ensureBooleanValue(operator, Logic.AND, v2);

		return LiteralValue.from(((LiteralValue) v1).getBool() && ((LiteralValue) v2).getBool());
	}

	public static LiteralValue or(EvalContext context, Node operator, Value v1, Value v2) {
		ensureBooleanValue(operator, Logic.OR, v1);
		ensureBooleanValue(operator, Logic.OR, v2);

		return LiteralValue.from(((LiteralValue) v1).getBool() || ((LiteralValue) v2).getBool());
	}

	public static LiteralValue xor(EvalContext context, Node operator, Value v1, Value v2) {
		ensureBooleanValue(operator, Logic.XOR, v1);
		ensureBooleanValue(operator, Logic.XOR, v2);

		return LiteralValue.from(((LiteralValue) v1).getBool() ^ ((LiteralValue) v2).getBool());
	}

	public static LiteralValue not(EvalContext context, Node operator, Value v) {
		ensureBooleanValue(operator, Logic.NOT, v);

		return LiteralValue.from(!((LiteralValue) v).getBool());
	}

	public static LiteralValue equal(EvalContext context, Node operator, Value v1, Value v2) {
		return LiteralValue.from(_equal(context, operator, v1, v2));
	}

	public static boolean _equal(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case BOOLEAN:
			switch (v2.getType()) {
			case BOOLEAN:
				return ((LiteralValue) v1).getBool() == ((LiteralValue) v2).getBool();
			default:
				return false;
			}
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return ((LiteralValue) v1).getInteger() == ((LiteralValue) v2).getInteger();
			case REAL:
				return ((LiteralValue) v1).getInteger() == ((LiteralValue) v2).getReal();
			default:
				return false;
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return ((LiteralValue) v1).getReal() == ((LiteralValue) v2).getInteger();
			case REAL:
				return ((LiteralValue) v1).getReal() == ((LiteralValue) v2).getReal();
			default:
				return false;
			}
		case DATE_TIME:
			switch (v2.getType()) {
			case DATE_TIME:
				return ((LiteralValue) v1).getDate().equals(((LiteralValue) v2).getDate());
			default:
				return false;
			}
		case STRING:
			switch (v2.getType()) {
			case STRING:
				return ((LiteralValue) v1).getString().equals(((LiteralValue) v2).getString());
			default:
				return false;
			}
		case ARRAY:
			switch (v2.getType()) {
			case ARRAY:
				Array array1 = (Array) v1;
				Array array2 = (Array) v2;

				if (array1.getSize() != array2.getSize()) {
					return false;
				}

				for (int i = 0; i < array1.getSize(); i++) {
					if (!_equal(context, operator, array1.get(i).eval(context), array2.get(i).eval(context))) {
						return false;
					}
				}

				return true;
			default:
				return false;
			}
		default:
			return false;
		}
	}

	public static LiteralValue notEqual(EvalContext context, Node operator, Value v1, Value v2) {
		return LiteralValue.from(!_equal(context, operator, v1, v2));
	}

	public static LiteralValue less(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return LiteralValue.from(((LiteralValue) v1).getInteger() < ((LiteralValue) v2).getInteger());
			case REAL:
				return LiteralValue.from(((LiteralValue) v1).getInteger() < ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return LiteralValue.from(((LiteralValue) v1).getReal() < ((LiteralValue) v2).getInteger());
			case REAL:
				return LiteralValue.from(((LiteralValue) v1).getReal() < ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case DATE_TIME:
			switch (v2.getType()) {
			case DATE_TIME:
				return LiteralValue.from(((LiteralValue) v1).getDate().before(((LiteralValue) v2).getDate()));
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case STRING:
			switch (v2.getType()) {
			case STRING:
				return LiteralValue
						.from(((LiteralValue) v1).getString().compareTo(((LiteralValue) v2).getString()) < 0);
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
		}
	}

	public static LiteralValue lessEqual(EvalContext context, Node operator, Value v1, Value v2) {
		return LiteralValue.from(!greater(context, operator, v1, v2).getBool());
	}

	public static LiteralValue greater(EvalContext context, Node operator, Value v1, Value v2) {
		switch (v1.getType()) {
		case INTEGER:
			switch (v2.getType()) {
			case INTEGER:
				return LiteralValue.from(((LiteralValue) v1).getInteger() > ((LiteralValue) v2).getInteger());
			case REAL:
				return LiteralValue.from(((LiteralValue) v1).getInteger() > ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case REAL:
			switch (v2.getType()) {
			case INTEGER:
				return LiteralValue.from(((LiteralValue) v1).getReal() > ((LiteralValue) v2).getInteger());
			case REAL:
				return LiteralValue.from(((LiteralValue) v1).getReal() > ((LiteralValue) v2).getReal());
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case DATE_TIME:
			switch (v2.getType()) {
			case DATE_TIME:
				return LiteralValue.from(((LiteralValue) v1).getDate().after(((LiteralValue) v2).getDate()));
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		case STRING:
			switch (v2.getType()) {
			case STRING:
				return LiteralValue
						.from(((LiteralValue) v1).getString().compareTo(((LiteralValue) v2).getString()) > 0);
			default:
				throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
			}
		default:
			throw new IncompatibleTypesException(operator, Comparison.LESS, v1, v2);
		}
	}

	public static LiteralValue greaterEqual(EvalContext context, Node operator, Value v1, Value v2) {
		return LiteralValue.from(!less(context, operator, v1, v2).getBool());
	}

	public static boolean evalBoolean(EvalContext context, Node operator, Value value) {
		ensureBooleanValue(operator, null, value);

		return ((LiteralValue) value).getBool();
	}

	public static long evalInteger(EvalContext context, Node operator, Value value) {
		ensureIntegerValue(operator, null, value);

		return ((LiteralValue) value).getInteger();
	}

	private static void ensureBooleanValue(Node node, Operator<?> operator, Value v) {
		if (v.getType() != ValueType.BOOLEAN) {
			throw new IncompatibleTypesException(node, operator, v);
		}
	}

	private static void ensureIntegerValue(Node node, Operator<?> operator, Value v) {
		if (v.getType() != ValueType.INTEGER) {
			throw new IncompatibleTypesException(node, operator, v);
		}
	}
}
