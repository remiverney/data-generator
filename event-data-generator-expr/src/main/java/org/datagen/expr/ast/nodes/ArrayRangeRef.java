package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.exception.NotAnArrayException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class ArrayRangeRef implements Node {

	private final Node array;
	private final Node start;
	private final Node end;

	public ArrayRangeRef(Node array, Node start, Node end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	public Node getArray() {
		return array;
	}

	@Override
	public List<Node> getChildren() {
		return Arrays.asList(array, start, end);
	}

	@Override
	public Value eval(EvalContext context) {
		Value resolved = array.eval(context);

		if (!resolved.isArray()) {
			throw new NotAnArrayException(this, resolved.getType());
		}

		return new ArrayDef(((Array) resolved)
				.getRange(
						ValueOperation.evalInteger(context, this,
								start.eval(context)),
						ValueOperation.evalInteger(context, this,
								end.eval(context))).stream()
				.map(x -> x.eval(context))
				.collect(Collectors.toCollection(ArrayList::new)));
	}
}
