package org.datagen.expr.ast.derivative;

import org.datagen.expr.ast.exception.NonDerivableExpressionException;
import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.nodes.ArithmeticOp;
import org.datagen.expr.ast.nodes.LiteralValue;
import org.datagen.expr.ast.nodes.Negation;

public final class DerivationOperation {

	@FunctionalInterface
	public interface Evaluator {
		Node derivative(DerivationContext context, ArithmeticOp operator);
	}

	private DerivationOperation() {
	}

	public static Node nonDerivable(DerivationContext context, ArithmeticOp operator) {
		throw new NonDerivableExpressionException(operator);
	}

	public static Node add(DerivationContext context, ArithmeticOp operator) {
		return add(context, operator.getLhs(), operator.getRhs());
	}

	private static Node add(DerivationContext context, Node lhs, Node rhs) {
		Node dlhs = context.derive(lhs);
		Node drhs = context.derive(rhs);

		if (dlhs == LiteralValue.ZERO) {
			return drhs;
		} else if (drhs == LiteralValue.ZERO) {
			return dlhs;
		} else {
			return new ArithmeticOp(dlhs, drhs, Arithmetic.ADD).optimize(context.getEvalContext());
		}
	}

	public static Node sub(DerivationContext context, ArithmeticOp operator) {
		return sub(context, operator.getLhs(), operator.getRhs());
	}

	private static Node sub(DerivationContext context, Node lhs, Node rhs) {
		Node dlhs = context.derive(lhs);
		Node drhs = context.derive(rhs);

		if (dlhs == LiteralValue.ZERO) {
			return new Negation(drhs);
		} else if (drhs == LiteralValue.ZERO) {
			return dlhs;
		} else {
			return new ArithmeticOp(dlhs, drhs, Arithmetic.SUB).optimize(context.getEvalContext());
		}
	}

	public static Node mul(DerivationContext context, ArithmeticOp operator) {
		return mul(context, operator.getLhs(), operator.getRhs());
	}

	private static Node mul(DerivationContext context, Node lhs, Node rhs) {
		Node dlhs = context.derive(lhs);
		Node drhs = context.derive(rhs);

		if (dlhs == LiteralValue.ZERO) {
			if (drhs == LiteralValue.ZERO) {
				return LiteralValue.ZERO;
			} else {
				return new ArithmeticOp(lhs, drhs, Arithmetic.MUL).optimize(context.getEvalContext());
			}
		} else {
			if (drhs == LiteralValue.ZERO) {
				return new ArithmeticOp(dlhs, rhs, Arithmetic.MUL).optimize(context.getEvalContext());
			} else {
				return new ArithmeticOp(new ArithmeticOp(lhs, drhs, Arithmetic.MUL), new ArithmeticOp(dlhs, rhs,
						Arithmetic.MUL), Arithmetic.ADD).optimize(context.getEvalContext());
			}
		}
	}

	public static Node div(DerivationContext context, ArithmeticOp operator) {
		return div(context, operator.getLhs(), operator.getRhs());
	}

	private static Node div(DerivationContext context, Node lhs, Node rhs) {
		Node dlhs = context.derive(lhs);
		Node drhs = context.derive(rhs);

		if (dlhs == LiteralValue.ZERO) {
			if (drhs == LiteralValue.ZERO) {
				return LiteralValue.ZERO;
			} else {
				// return new ArithmeticOp(lhs, drhs, Arithmetic.MUL);
			}
		} else {
			if (drhs == LiteralValue.ZERO) {
				// return new ArithmeticOp(dlhs, rhs, Arithmetic.MUL);
			} else {
				// return new ArithmeticOp(new ArithmeticOp(lhs, drhs,
				// Arithmetic.MUL), new ArithmeticOp(dlhs, rhs,
				// Arithmetic.MUL), Arithmetic.ADD);
			}
		}

		throw new UnsupportedOperationException("not implemented");
	}

	public static Node neg(DerivationContext context, ArithmeticOp operator) {
		return neg(context, operator.getRhs());
	}

	private static Node neg(DerivationContext context, Node rhs) {
		Node drhs = context.derive(rhs);

		if (drhs instanceof Negation) {
			return ((Negation) drhs).getRhs();
		} else {
			return new Negation(drhs).optimize(context.getEvalContext());
		}
	}

	public static Node pow(DerivationContext context, ArithmeticOp operator) {
		return pow(context, operator.getLhs(), operator.getRhs());
	}

	// (f^g)' = (g*f'/f + g'*ln(f)) * f^g
	@SuppressWarnings("unused")
	private static Node pow(DerivationContext context, Node lhs, Node rhs) {
		Node dlhs = context.derive(lhs);
		Node drhs = context.derive(rhs);

		throw new UnsupportedOperationException("not implemented");
	}

}
