package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.datagen.expr.ast.Mapped;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.UnknownFieldException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.format.ValueFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.expr.ast.intf.ValueType;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class MappedDef implements Value, Mapped {

	private final Map<Node, Node> items;

	public MappedDef(Map<Node, Node> items) {
		this.items = items;
	}

	@Override
	public Map<Node, Node> getAll() {
		return items;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>(2 * items.size());

		for (Entry<Node, Node> item : items.entrySet()) {
			children.add(item.getKey());
			children.add(item.getValue());
		}

		return children;
	}

	@Override
	public boolean isMapped() {
		return true;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public Node get(Value key) {
		throw new UnsupportedOperationException("Attempted to get a field from an associative map not evaluated before");
	}

	@Override
	public List<? extends Node> getValues() {
		return new ArrayList<>(items.values());
	}

	@Override
	public List<? extends Node> getKeys() {
		return new ArrayList<>(items.keySet());
	}

	@Override
	public Iterator<Node> iterator() {
		return getChildren().iterator();
	}

	@Override
	public ValueType getType() {
		return ValueType.MAPPED;
	}

	@Override
	public MappedDef eval(EvalContext context) {
		return new MappedDef(items.entrySet().stream()
				.collect(Collectors.toMap(x -> x.getKey().eval(context), x -> x.getValue().eval(context)))) {
			@Override
			public Node get(Value key) {
				Node value = MappedDef.this.items.get(key);
				if (value == null) {
					throw new UnknownFieldException(MappedDef.this, key.toString());
				}

				return value.eval(context);
			}
		};
	}

	@Override
	public String toValueString(ValueFormatContext context) {
		return context.formatMapped(this);
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		builder.append('{');
		context.spacing(builder);

		context.formatList(
				builder,
				items.entrySet(),
				',',
				x -> builder.append(x.getKey().toString(builder, context).append(" => ")
						.append(x.getValue().toString(builder, context))));

		context.spacing(builder);
		builder.append('}');

		return builder;
	}
}
