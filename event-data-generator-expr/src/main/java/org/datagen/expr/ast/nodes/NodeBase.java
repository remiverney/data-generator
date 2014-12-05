package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public abstract class NodeBase implements Node {

	private final int line;
	private final int column;

	protected NodeBase() {
		this(0, 0);
	}

	protected NodeBase(int line, int column) {
		this.line = line;
		this.column = column;
	}

	@Override
	public int getSourceLine() {
		return this.line;
	}

	@Override
	public int getSourceCol() {
		return this.column;
	}

}
