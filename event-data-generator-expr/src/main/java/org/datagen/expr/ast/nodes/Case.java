package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import org.datagen.expr.ast.EvalContext;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.exception.NoMatchingCaseException;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;

public class Case implements Node {

	private final Node expr;
	private final List<CaseWhen> cases;
	private final Node otherwise;

	public Case(List<CaseWhen> cases) {
		this(null, cases, null);
	}

	public Case(Node expr, List<CaseWhen> cases) {
		this(expr, cases, null);
	}

	public Case(Node expr, List<CaseWhen> cases, Node otherwise) {
		this.expr = expr;
		this.cases = cases;
		this.otherwise = otherwise;
	}

	public Node getExpr() {
		return expr;
	}

	public List<CaseWhen> getCases() {
		return cases;
	}

	public Node getOtherwise() {
		return otherwise;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();

		if (expr != null) {
			children.add(expr);
		}

		children.addAll(cases);
		if (otherwise != null) {
			children.add(otherwise);
		}

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		if (expr != null) {
			Value test = expr.eval(context);
			for (CaseWhen caze : cases) {
				Value value = caze.getWhen().eval(context);
				if (ValueOperation.evalBoolean(context, this,
						ValueOperation.equal(context, this, test, value))) {
					return caze.getThen().eval(context);
				}
			}

			if (otherwise != null) {
				return otherwise.eval(context);
			} else {
				throw new NoMatchingCaseException(this, test);
			}
		} else {
			for (CaseWhen caze : cases) {
				Value value = caze.getWhen().eval(context);
				if (ValueOperation.evalBoolean(context, this, value)) {
					return caze.getThen().eval(context);
				}
			}

			if (otherwise != null) {
				return otherwise.eval(context);
			} else {
				throw new NoMatchingCaseException(this);
			}
		}
	}

}
