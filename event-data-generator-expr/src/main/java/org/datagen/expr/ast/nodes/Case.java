package org.datagen.expr.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.datagen.expr.ast.Keywords;
import org.datagen.expr.ast.ValueOperation;
import org.datagen.expr.ast.context.EvalContext;
import org.datagen.expr.ast.exception.NoMatchingCaseException;
import org.datagen.expr.ast.format.ExpressionFormatContext;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.Value;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class Case implements Node {

	private final Optional<Node> expr;
	private final List<CaseWhen> cases;
	private final Optional<Node> otherwise;

	public Case(List<CaseWhen> cases) {
		this(Optional.empty(), cases, Optional.empty());
	}

	public Case(Node expr, List<CaseWhen> cases) {
		this(Optional.of(expr), cases, Optional.empty());
	}

	public Case(Node expr, List<CaseWhen> cases, Node otherwise) {
		this(Optional.of(expr), cases, Optional.of(otherwise));
	}

	private Case(Optional<Node> expr, List<CaseWhen> cases, Optional<Node> otherwise) {
		this.expr = expr;
		this.cases = cases;
		this.otherwise = otherwise;
	}

	public Optional<Node> getExpr() {
		return expr;
	}

	public List<CaseWhen> getCases() {
		return cases;
	}

	public Optional<Node> getOtherwise() {
		return otherwise;
	}

	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();

		expr.ifPresent(x -> children.add(x));
		children.addAll(cases);
		otherwise.ifPresent(x -> children.add(x));

		return children;
	}

	@Override
	public Value eval(EvalContext context) {
		if (expr.isPresent()) {
			Value test = expr.get().eval(context);
			for (CaseWhen caze : cases) {
				Value value = caze.getWhen().eval(context);
				if (ValueOperation.evalBoolean(context, this, ValueOperation.equal(context, this, test, value))) {
					return caze.getThen().eval(context);
				}
			}

			if (otherwise.isPresent()) {
				return otherwise.get().eval(context);
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

			if (otherwise.isPresent()) {
				return otherwise.get().eval(context);
			} else {
				throw new NoMatchingCaseException(this);
			}
		}
	}

	@Override
	public StringBuilder toString(StringBuilder builder, ExpressionFormatContext context) {
		context.newline(builder);
		context.formatKeyword(builder, Keywords.CASE);

		if (expr.isPresent()) {
			context.spacing(builder);
			expr.get().toString(builder, context);
		}

		context.nest();

		for (CaseWhen caze : cases) {
			context.newline(builder);
			caze.toString(builder, context);
		}

		if (otherwise.isPresent()) {
			context.newline(builder);
			context.formatKeyword(builder, Keywords.ELSE);
			context.spacing(builder);
			otherwise.get().toString(builder, context);
		}

		context.unnest();
		context.newline(builder);
		context.formatKeyword(builder, Keywords.END);

		return builder;
	}

}
