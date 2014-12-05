package org.datagen.expr.ast;

import java.util.Map;
import java.util.Set;

import org.datagen.expr.ast.intf.Node;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class LibraryImpl implements Library {

	private final String name;
	private final Map<String, Node> entries;

	public LibraryImpl(String name, Map<String, Node> entries) {
		this.name = name;
		this.entries = entries;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Node get(String entry) {
		return entries.get(entry);
	}

	@Override
	public Set<String> getEntries() {
		return this.entries.keySet();
	}

}
