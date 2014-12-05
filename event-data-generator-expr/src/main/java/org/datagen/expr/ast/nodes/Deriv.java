package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.Lambda;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationResult.StatusLevel;
import org.datagen.expr.ast.derivative.DerivationContextimpl;
import org.datagen.expr.ast.exception.IncompatibleArgumentException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class Deriv implements Node {

	private final Node expr;
	private final Node variable;

	public Deriv(Node expr, Node variable) {
		this.expr = expr;
		this.variable = variable;
	}

	@Override
	public Value eval(EvalContext context) {
		Value value = variable.eval(context);

		if (value.getType() != ValueType.STRING) {
			throw new IncompatibleArgumentException(this, 1, value.getType(), ValueType.STRING);
		}

		Value func = expr.eval(context);

		if (value.getType() != ValueType.LAMBDA) {
			throw new IncompatibleArgumentException(this, 2, func.getType(), ValueType.LAMBDA);
		}

		return new DerivationContextimpl(((LiteralValue) value).getString(), context).derive(((Lambda) func).getBody())
				.eval(context);
	}

	public Node getExpr() {
		return expr;
	}

	public Node getVariable() {
		return variable;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(variable);
		children.add(expr);

		return children;
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		context.formatKeyword(builder, Keywords.DERIV);

		builder.append('(');
		variable.toString(builder, context);
		builder.append(',');
		context.spacing(builder);
		expr.toString(builder, context);
		builder.append(')');

		return builder;
	}

	@Override
	public void validate(ValidationContext context) {
		if (variable instanceof Value) {
			if (((Value) expr).getType() != ValueType.STRING) {
				context.addStatus(StatusLevel.ERROR,
						new IncompatibleArgumentException(this, 1, ((Value) expr).getType(), ValueType.STRING));
			}
		}

		if (expr instanceof Value) {
			if (((Value) expr).getType() != ValueType.LAMBDA) {
				context.addStatus(StatusLevel.ERROR,
						new IncompatibleArgumentException(this, 2, ((Value) expr).getType(), ValueType.LAMBDA));
			}
		}

		Node.super.validate(context);
	}

}
