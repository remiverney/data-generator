package org.datagen.expr.ast.nodes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationResult.StatusLevel;
import org.datagen.expr.ast.exception.DynamicEvaluationException;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.exception.ParsingException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.expr.parser.Parser;
import org.datagen.expr.parser.ParserResult;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class Eval implements Node {

	private final Node expr;
	private final Optional<ClassLoader> loader;

	public Eval(Node expr) {
		this(expr, Optional.<ClassLoader> empty());
	}

	public Eval(Node expr, Optional<ClassLoader> loader) {
		this.expr = expr;
		this.loader = loader;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = expr.eval(context);

		if (value.getType() != ValueType.STRING) {
			throw new IncompatibleArgumentException(this, 1, value.getType(), ValueType.STRING);
		}

		return eval(((LiteralValue) value).getString(), context);
	}

	public Node getExpr() {
		return expr;
	}

	private Value eval(String string, EvalContext context) {
		try {
			ParserResult result = Parser.parse(string, context.getInterpreter().getConfiguration(), context,
					this.loader);
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
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.EVAL);

		builder.append('(');
		expr.toString(builder, context);
		builder.append(')');

		return builder;
	}

	@Override
	public void validate(ValidationContext context) {
		if (expr instanceof Value) {
			if (((Value) expr).getType() != ValueType.STRING) {
				context.addStatus(StatusLevel.ERROR,
						new IncompatibleArgumentException(this, 1, ((Value) expr).getType(), ValueType.STRING));
			}
		}
		Node.super.validate(context);
	}

	@Override
	public Node optimize(EvalContext context) {
		if ((expr instanceof Value) && (((Value) expr).getType() == ValueType.STRING)) {
			try {
				ParserResult result = Parser.parse(((LiteralValue) expr).getString(), context.getInterpreter()
						.getConfiguration(), context, this.loader);
				return result.getRoot();
			} catch (ParsingException e) {
				throw new DynamicEvaluationException(this, ((LiteralValue) expr).getString(), e);
			}
		} else {
			return Node.super.optimize(context);
		}
	}

}
