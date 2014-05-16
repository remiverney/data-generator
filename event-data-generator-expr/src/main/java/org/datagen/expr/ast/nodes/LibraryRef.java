package org.datagen.expr.ast.nodes;

import org.datagen.expr.ast.ExpressionFormatContext;
import org.datagen.expr.ast.Library;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.UnresolvedReferenceException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Reference;
import org.datagen.expr.ast.intf.Value;

public class LibraryRef implements Reference {

	private final String library;
	private final String entry;

	public LibraryRef(String library, String entry) {
		this.library = library;
		this.entry = entry;
	}

	@Override
	public Value eval(EvalContext context) {
		Library lib = context.getLibrary(library);
		if (lib == null) {
			throw new UnresolvedReferenceException(this, library);
		}

		Node node = lib.get(entry);
		if (node == null) {
			throw new UnresolvedReferenceException(this, entry);
		}

		return node.eval(context);
	}

	@Override
	public String getReference() {
		return library + ":" + entry;
	}

	@Override
	public StringBuilder toString(StringBuilder builder,
			ExpressionFormatContext context) {
		return builder.append(library).append(':').append(entry);
	}
}
