package org.datagen.expr.ast.intf;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.datagen.expr.ast.nodes.ArrayDef;
import org.datagen.expr.ast.nodes.CaseWhen;

public interface NodeFactory {

	@Nonnull
	Node buildArithmeticOp(Node lhs, Node rhs, Arithmetic operator);

	@Nonnull
	Node buildArrayDef(List<? extends Node> items);

	@Nonnull
	Node buildArrayRangeRef(Node array, Node start, Node end);

	@Nonnull
	Node buildArrayRef(Node array, Node index);

	@Nonnull
	Node buildAttrRef(Node expr, String attribute);

	@Nonnull
	Node buildCase(List<CaseWhen> cases);

	@Nonnull
	Node buildCase(Node expr, List<CaseWhen> cases);

	@Nonnull
	Node buildCase(Node expr, List<CaseWhen> cases, Node otherwise);

	@Nonnull
	CaseWhen buildCaseWhen(Node when, Node then);

	@Nonnull
	Node buildComparisonOp(Node lhs, Node rhs, Comparison operator);

	@Nonnull
	Node buildDeriv(Node expr, Node variable);

	@Nonnull
	Node buildEval(Node expr);

	@Nonnull
	Node buildFactorial(Node rhs);

	@Nonnull
	Node buildFieldRef(String field);

	@Nonnull
	Node buildFunctionCall(String name, List<Node> parameters);

	@Nonnull
	Node buildJavaRef(List<String> fqn);

	@Nonnull
	Node buildJavaRef(List<String> fqn, List<Node> parameters);

	@Nonnull
	Node buildJavaRef(String fqn);

	@Nonnull
	Node buildJavaRef(String fqn, List<Node> parameters);

	@Nonnull
	Node buildLambdaCall(Node lambda, List<Node> parameters);

	@Nonnull
	Node buildLambdaDef(List<String> parameters, Node body);

	@Nonnull
	Node buildLibraryRef(String library, String entry);

	@Nonnull
	Node buildLogicOp(Node lhs, Node rhs, Logic operator);

	@Nonnull
	Node buildMappedDef(Map<Node, Node> items);

	@Nonnull
	Node buildNegation(Node rhs);

	@Nonnull
	Node buildNot(Node rhs);

	@Nonnull
	Node buildParallel(ArrayDef expr, Optional<Node> reducer);

	@Nonnull
	Node buildPropertyRef(String property);

	@Nonnull
	Node buildTernary(Node condition, Node then, Node otherwise);

	@Nonnull
	Node buildThisRef();

	@Nonnull
	Node buildTypeOf(Node expr);

	@Nonnull
	Node buildVariableRef(String variable);
}
