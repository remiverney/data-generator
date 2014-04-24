package org.datagen.expr.ast.nodes;

import java.util.Arrays;
import java.util.List;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.DefaultValueFormatContext;
import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.exception.NotAnArrayException;
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

		if (!resolved.isArray()) {
			throw new NotAnArrayException(this, resolved.getType());
		}

		System.out
				.println("access idx "
						+ ValueOperation.evalInteger(context, this,
								index.eval(context))
						+ " of array "
						+ resolved
								.toValueString(new DefaultValueFormatContext()));

		return ((Array) resolved).get(
				ValueOperation.evalInteger(context, this, index.eval(context)))
				.eval(context);
	}
}
