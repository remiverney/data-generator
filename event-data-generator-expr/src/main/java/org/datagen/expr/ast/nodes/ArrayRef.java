package org.datagen.expr.ast.nodes;

import java.util.Arrays;
import java.util.List;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.Mapped;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.context.ValidationContext;
import org.datagen.expr.ast.context.ValidationResult.StatusLevel;
import org.datagen.expr.ast.exception.NotAnArrayException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class ArrayRef implements Node {

	private final Node array;
	private final Node index;

	public ArrayRef(Node array, Node index) {
		this.array = array;
		this.index = index;
	}

	public Node getArray() {
		return array;
	}

	public Node getIndex() {
		return index;
	}

	@Override
	public List<Node> getChildren() {
		return Arrays.asList(array, index);
	}

	@Override
	public Value eval(EvalContext context) {
		Value resolved = array.eval(context);

		switch (resolved.getType()) {
		case ARRAY:
			return ((Array) resolved).get(ValueOperation.evalInteger(context, this, index.eval(context))).eval(context);
		case MAPPED:
			return ((Mapped) resolved).get(index.eval(context)).eval(context);
		default:
			throw new NotAnArrayException(this, resolved.getType());
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		array.toString(builder, context).append('[');
		index.toString(builder, context).append(']');

		return builder;
	}

	@Override
	public Node optimize(EvalContext context) {
		if ((index instanceof LiteralValue) && (array instanceof Array)) {
			return ((Array) array).get(ValueOperation.evalInteger(context, this, index.eval(context)));
		} else if ((index instanceof LiteralValue) && (array instanceof Mapped)) {
			return ((Mapped) array).get(index.eval(context)).eval(context);
		} else {
			return Node.super.optimize(context);
		}
	}

	@Override
	public void validate(ValidationContext context) {
		if (index instanceof Value) {
			if (!(array instanceof Array || array instanceof Mapped)) {
				context.addStatus(StatusLevel.ERROR, new NotAnArrayException(this, ((Value) index).getType()));
			}
		}
	}
}
