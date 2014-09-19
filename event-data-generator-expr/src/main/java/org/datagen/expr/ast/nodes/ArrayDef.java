package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Array;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.OutBoundsArrayException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;

public class ArrayDef implements Value, Array {

	private final List<Node> items;

	@SuppressWarnings("unchecked")
	public ArrayDef(List<? extends Node> items) {
		this.items = (List<Node>) items;
	}

	public List<Node> getItems() {
		return items;
	}

	@Override
	public List<Node> getChildren() {
		return items;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public Node get(long index) {
		if (index >= items.size() || index < 0) {
			throw new OutBoundsArrayException(this, items.size(), index);
		}

		return items.get((int) index);
	}

	@Override
	public List<Node> getRange(long start, long end) {
		if (start >= items.size() || start < 0) {
			throw new OutBoundsArrayException(this, items.size(), start);
		}

		if (end == start - 1) {
			return Collections.<Node> emptyList();
		} else {
			if (end >= items.size() || end < 0) {
				throw new OutBoundsArrayException(this, items.size(), end);
			}

			if (start > end) {
				throw new OutBoundsArrayException(this, items.size(), end);
			}

			return items.subList((int) start, (int) (end + 1));
		}
	}

	@Override
	public List<? extends Node> getAll() {
		return items;
	}

	@Override
	public Iterator<Node> iterator() {
		return items.iterator();
	}

	@Override
	public ValueType getType() {
		return ValueType.ARRAY;
	}

	@Override
	public ArrayDef eval(EvalContext context) {
		return new ArrayDef(items.stream().map(i -> i.eval(context))
				.collect(Collectors.toCollection(ArrayList::new)));
	}

	@Override
	public String toValueString(ValueFormatContext context) {
		return context.formatArray(this);
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		builder.append('{');
		context.spacing(builder);

		context.formatList(builder, items, ',');

		context.spacing(builder);
		builder.append('}');

		return builder;
	}

	@Override
	public ArrayDef optimize(EvalContext context) {
		return this;
	}
}
