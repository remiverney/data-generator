package org.datagen.expr.ast.nodes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.datagen.expr.ast.intf.Arithmetic;
import org.datagen.expr.ast.intf.Comparison;
import org.datagen.expr.ast.intf.Logic;
import org.datagen.expr.ast.intf.Node;
import org.datagen.expr.ast.intf.NodeFactory;
import org.datagen.expr.interpreter.InterpreterParameters;
import org.datagen.factory.Config;
import org.datagen.utils.annotation.Immutable;

@Immutable
public class NodeFactoryImpl implements NodeFactory {

	private final Optional<Config<InterpreterParameters>> configuration;
	private final Optional<ClassLoader> classLoader;

	public NodeFactoryImpl(Optional<Config<InterpreterParameters>> configuration, Optional<ClassLoader> classLoader) {
		this.configuration = configuration;
		this.classLoader = classLoader;
	}

	@Override
	public Node buildArithmeticOp(Node lhs, Node rhs, Arithmetic operator) {
		return new ArithmeticOp(lhs, rhs, operator);
	}

	@Override
	public Node buildArrayDef(List<? extends Node> items) {
		return new ArrayDef(items);
	}

	@Override
	public Node buildArrayRangeRef(Node array, Node start, Node end) {
		return new ArrayRangeRef(array, start, end);
	}

	@Override
	public Node buildArrayRef(Node array, Node index) {
		return new ArrayRef(array, index);
	}

	@Override
	public Node buildAttrRef(Node expr, String attribute) {
		return new AttrRef(expr, attribute);
	}

	@Override
	public Node buildCase(List<CaseWhen> cases) {
		return new Case(cases);
	}

	@Override
	public Node buildCase(Node expr, List<CaseWhen> cases) {
		return new Case(expr, cases);
	}

	@Override
	public Node buildCase(Node expr, List<CaseWhen> cases, Node otherwise) {
		return new Case(expr, cases, otherwise);
	}

	@Override
	public CaseWhen buildCaseWhen(Node when, Node then) {
		return new CaseWhen(when, then);
	}

	@Override
	public Node buildComparisonOp(Node lhs, Node rhs, Comparison operator) {
		return new ComparisonOp(lhs, rhs, operator);
	}

	@Override
	public Node buildDeriv(Node expr, Node variable) {
		return new Deriv(expr, variable);
	}

	@Override
	public Node buildEval(Node expr) {
		return new Eval(expr);
	}

	@Override
	public Node buildFactorial(Node rhs) {
		return new Factorial(rhs);
	}

	@Override
	public Node buildFieldRef(String field) {
		return new FieldRef(field);
	}

	@Override
	public Node buildFunctionCall(String name, List<Node> parameters) {
		return new FunctionCall(name, parameters);
	}

	@Override
	public Node buildJavaRef(List<String> fqn) {
		return new JavaRef(fqn);
	}

	@Override
	public Node buildJavaRef(List<String> fqn, List<Node> parameters) {
		return new JavaRef(fqn, parameters);
	}

	@Override
	public Node buildJavaRef(String fqn) {
		return new JavaRef(fqn);
	}

	@Override
	public Node buildJavaRef(String fqn, List<Node> parameters) {
		return new JavaRef(fqn, parameters);
	}

	@Override
	public Node buildLambdaCall(Node lambda, List<Node> parameters) {
		return new LambdaCall(lambda, parameters);
	}

	@Override
	public Node buildLambdaDef(List<String> parameters, Node body) {
		return new LambdaDef(parameters, body);
	}

	@Override
	public Node buildLibraryRef(String library, String entry) {
		return new LibraryRef(library, entry);
	}

	@Override
	public Node buildLogicOp(Node lhs, Node rhs, Logic operator) {
		return new LogicOp(lhs, rhs, operator);
	}

	@Override
	public Node buildMappedDef(Map<Node, Node> items) {
		return new MappedDef(items);
	}

	@Override
	public Node buildNegation(Node rhs) {
		return new Negation(rhs);
	}

	@Override
	public Node buildNot(Node rhs) {
		return new Not(rhs);
	}

	@Override
	public Node buildParallel(ArrayDef expr, Optional<Node> reducer) {
		return new Parallel(expr, reducer);
	}

	@Override
	public Node buildPropertyRef(String property) {
		return new PropertyRef(property);
	}

	@Override
	public Node buildTernary(Node condition, Node then, Node otherwise) {
		return new Ternary(condition, then, otherwise);
	}

	@Override
	public Node buildThisRef() {
		return new ThisRef();
	}

	@Override
	public Node buildTypeOf(Node expr) {
		return new TypeOf(expr);
	}

	@Override
	public Node buildVariableRef(String variable) {
		return new VariableRef(variable);
	}

}
