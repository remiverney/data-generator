package org.datagen.expr.ast.functions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Operator;
import org.datagen.expr.ast.nodes.BinaryOp;
import org.datagen.expr.ast.nodes.LambdaDef;
import org.datagen.expr.ast.nodes.VariableRef;

public final class ShortOperators {

	@SuppressWarnings("rawtypes")
	private static final class ShortOperatorOp extends BinaryOp {

		@SuppressWarnings("unchecked")
		public ShortOperatorOp(Node lhs, Node rhs, Operator<?> operator) {
			super(lhs, rhs, (Enum) operator);
		}
	}

	private static final String OPERAND_LEFT = "x";
	private static final String OPERAND_RIGHT = "y";

	public static final LambdaDef ADD = buildLambda(Arithmetic.ADD);
	public static final LambdaDef SUB = buildLambda(Arithmetic.SUB);
	public static final LambdaDef MUL = buildLambda(Arithmetic.MUL);
	public static final LambdaDef DIV = buildLambda(Arithmetic.DIV);
	public static final LambdaDef MOD = buildLambda(Arithmetic.MOD);

	public static final LambdaDef AND = buildLambda(Logic.AND);
	public static final LambdaDef OR = buildLambda(Logic.OR);

	private static final Map<String, LambdaDef> SHORT_OPERATOR_MAP = new HashMap<>();

	static {
		SHORT_OPERATOR_MAP.put("+", ADD);
		SHORT_OPERATOR_MAP.put("-", SUB);
		SHORT_OPERATOR_MAP.put("*", MUL);
		SHORT_OPERATOR_MAP.put("/", DIV);
		SHORT_OPERATOR_MAP.put("%", MOD);
		SHORT_OPERATOR_MAP.put("&&", AND);
		SHORT_OPERATOR_MAP.put("||", OR);
	}

	private ShortOperators() {
	}

	public static LambdaDef getShortOperator(String symbol) {
		return SHORT_OPERATOR_MAP.get(symbol);
	}

	private static LambdaDef buildLambda(Operator<?> operator) {
		return new LambdaDef(Arrays.asList(OPERAND_LEFT, OPERAND_RIGHT),
				new ShortOperatorOp(new VariableRef(OPERAND_LEFT),
						new VariableRef(OPERAND_RIGHT), operator));
	}
}
