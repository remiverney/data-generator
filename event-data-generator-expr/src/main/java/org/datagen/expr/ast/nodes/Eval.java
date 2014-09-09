package org.datagen.expr.ast.nodes;

import java.util.Collections;
import java.util.List;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.DynamicEvaluationException;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.expr.parser.Parser;
import org.datagen.expr.parser.ParserResult;

public class Eval implements Node {

	private final Node expr;

	public Eval(Node expr) {
		this.expr = expr;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = expr.eval(context);

		if (value.getType() != ValueType.STRING) {
			throw new IncompatibleArgumentException(this, 1, value.getType(),
					ValueType.STRING);
		}

		return eval(((LiteralValue) value).getString(), context);
	}

	public Node getExpr() {
		return expr;
	}

	private Value eval(String string, EvalContext context) {
		try {
			ParserResult result = Parser.parse(string, context.getInterpreter()
					.getConfiguration(), context);
			return result.getRoot().eval(context);
		} catch (ParsingException e) {
			throw new DynamicEvaluationException(this, string, e);
		}
	}

	@Override
	public List<Node> getChildren() {
		return Collections.singletonList(expr);
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.EVAL);

		builder.append('(');
		expr.toString(builder, context);
		builder.append(')');

		return builder;
	}

}
