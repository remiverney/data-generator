package org.datagen.expr.ast;

import java.io.PrintStream;
import java.util.ListIterator;

import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Visitable.Visitor;

public final class AstWalker {

	private AstWalker() {
	}

	private static <T extends Node, U extends Node> void walkChildren(T node,
			Class<U> clazz, Visitor<U> visitor, boolean descending) {
		for (ListIterator<Node> iter = node.getChildren().listIterator(); iter
				.hasNext();) {
			Node child = iter.next();

			Node updated = AstWalker.<Node, U> walk(child, clazz, visitor,
					descending);
			if (updated != child) {
				iter.set(updated);
			}
		}
	}

	public static <T extends Node> T walk(T node, Visitor<Node> visitor,
			boolean descending) {
		return walk(node, Node.class, visitor, descending);
	}

	public static <T extends Node, U extends Node> T walk(T node,
			Class<U> clazz, Visitor<U> visitor, boolean descending) {
		if (descending) {
			@SuppressWarnings("unchecked")
			T updated = clazz.isInstance(node) ? (T) node
					.visit((Visitor<Node>) visitor) : node;
			walkChildren(node, clazz, visitor, descending);

			return updated;
		} else {
			walkChildren(node, clazz, visitor, descending);
			@SuppressWarnings("unchecked")
			T updated = clazz.isInstance(node) ? (T) node
					.visit((Visitor<Node>) visitor) : node;

			return updated;
		}
	}

	public static <T extends Node> void dump(T node, PrintStream s) {
		AstWalker.<Node> walk(node, x -> {
			s.println(x);
			return x;
		}, true);
	}
}
