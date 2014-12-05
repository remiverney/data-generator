grammar ExpressionParser;

@parser::header {
import java.util.*;

import org.datagen.expr.ast.*;
import org.datagen.expr.ast.intf.*;
import org.datagen.expr.ast.nodes.*;
import org.datagen.expr.ast.functions.ShortOperators;
}

@parser::members {
	private org.datagen.factory.Config<org.datagen.expr.interpreter.InterpreterParameters> configuration;
	private org.datagen.expr.ast.context.EvalContext context;
	private Optional<ClassLoader> loader = null;
	
	public void setConfiguration(org.datagen.factory.Config<org.datagen.expr.interpreter.InterpreterParameters> configuration) {
		this.configuration = configuration;
	}
	
	public void setEvalContext(org.datagen.expr.ast.context.EvalContext context) {
		this.context = context;
	}
	
	public void setJavaRefClassLoader(Optional<ClassLoader> loader) {
		this.loader = loader;
	}
	
	@FunctionalInterface
	private interface Optimizer<T extends Node> {
		T optimize(Node node);
	}
	
	private Optimizer<?> optimizer = (x -> configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ENABLE_OPTIMIZATIONS) ? x.optimize(context) : x);
}

import ExpressionLexer;

start returns [Node node]: e=expr EOF 
                           {$node = $e.node;}
;

expr returns [Node node]: p=primary
                          {$node = $p.node;}
                        | e=expr '.' Identifier
                          {$node = optimizer.optimize(new AttrRef($e.node, $Identifier.text));}
                        | l=expr '(' el=lambdaexprlist ')'
                          {$node = optimizer.optimize(new LambdaCall($l.node, $el.list));}
                        | a=expr '[' i=expr ']'
                          {$node = optimizer.optimize(new ArrayRef($a.node, $i.node));}
                        | a=expr '[' i=expr '..' i2=expr ']'
                          {$node = optimizer.optimize(new ArrayRangeRef($a.node, $i.node, $i2.node));}
                         | e=expr '!'
                          {$node = optimizer.optimize(new Factorial($e.node));}
                        | e1=expr '>>' e2=expr
                          {$node = optimizer.optimize(new ArithmeticOp($e1.node, $e2.node, Arithmetic.SHR));}
                        | e1=expr '<<' e2=expr
                          {$node = optimizer.optimize(new ArithmeticOp($e1.node, $e2.node, Arithmetic.SHL));}
                        | '+' e=expr
                          {$node = $e.node;}
                        | '-' e=expr
                          {$node = optimizer.optimize(new Negation($e.node));}
                        | e1=expr '^' e2=expr
                          {$node = optimizer.optimize(new ArithmeticOp($e1.node, $e2.node, Arithmetic.POW));}
                        | e1=expr { Arithmetic op = null; } ('*' {op = Arithmetic.MUL;} | '/' {op = Arithmetic.DIV;} | '%' {op = Arithmetic.MOD;}) e2=expr
                          {$node = optimizer.optimize(new ArithmeticOp($e1.node, $e2.node, op));}
                        | e1=expr { Arithmetic op = null; } ('+' {op = Arithmetic.ADD;} | '-' {op = Arithmetic.SUB;}) e2=expr
                          {$node = optimizer.optimize(new ArithmeticOp($e1.node, $e2.node, op));}
                        | e1=expr '<=' e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.LESS_EQUAL));}
                        | e1=expr '>=' e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.GREATER_EQUAL));}
                        | e1=expr '<' e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.LESS));}
                        | e1=expr '>' e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.GREATER));}
                        | e1=expr ('=' | '==') e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.EQUAL));}
                        | e1=expr ('!=' | '<>') e2=expr
                          {$node = optimizer.optimize(new ComparisonOp($e1.node, $e2.node, Comparison.NOT_EQUAL));}
                        | ('!' | 'NOT') e=expr
                          {$node = optimizer.optimize(new Not($e.node));}
                        | e1=expr ('AND' | '&&') e2=expr
                          {$node = optimizer.optimize(new LogicOp($e1.node, $e2.node, Logic.AND));}
                        | e1=expr ('OR' | '||') e2=expr
                          {$node = optimizer.optimize(new LogicOp($e1.node, $e2.node, Logic.OR));}
                        | e1=expr ('XOR' | '^^') e2=expr
                          {$node = optimizer.optimize(new LogicOp($e1.node, $e2.node, Logic.XOR));}
                        | <assoc=right> e1=expr '?' e2=expr ':' e3=expr
                          {$node = optimizer.optimize(new Ternary($e1.node, $e2.node, $e3.node));}
;

whenspec returns [CaseWhen node]: 'WHEN' when=expr 'THEN' then=expr
                                  {$node = (CaseWhen)optimizer.optimize(new CaseWhen($when.node, $then.node));}
;

primary returns [Node node]: '(' e=expr ')'
                             {$node = $e.node;}
                           | IntegerConstant
                             {$node = optimizer.optimize(LiteralValue.from(Long.parseLong($IntegerConstant.text)));}
                           | DecimalFloatingConstant
                             {$node = optimizer.optimize(new LiteralValue(Double.parseDouble($DecimalFloatingConstant.text)));}
                           | StringLiteral
                             {String string = $StringLiteral.text; $node = optimizer.optimize(new LiteralValue(string == null ? "" : string.substring(1, string.length() - 1)));}
                           | Identifier
                             {$node = optimizer.optimize(new VariableRef($Identifier.text));}
                           | 'this'
                             {$node = optimizer.optimize(new ThisRef());}
                           | 'true'
                             {$node = LiteralValue.TRUE;}
                           | 'false'
                             {$node = LiteralValue.FALSE;}
                           | 'TYPEOF' '(' e=expr ')'
                             {$node = optimizer.optimize(new TypeOf($e.node));}
                           | 'EVAL' '(' e=expr ')'
                             {$node = optimizer.optimize(new Eval($e.node, this.loader));}
                           | 'DERIV' '(' v=expr ',' e=expr ')'
                             {$node = optimizer.optimize(new Deriv($e.node, $v.node));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_PROPERTY_REFERENCE)}? '$' Identifier
                             {$node = optimizer.optimize(new PropertyRef($Identifier.text));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FIELD_REFERENCE)}? col=columnref
                             {$node = $col.fieldref;}
                           | 'PARALLEL' '(' '{' el=exprlist '}' { Node reducer = null; } ( ',' r=lambdaexpr { reducer = $r.node; } )? ')'
                             { $node = optimizer.optimize(new Parallel((ArrayDef)optimizer.optimize(new ArrayDef($el.list)), reducer)); }
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LAMBDA_DEFINITION)}? lm=lambda
                             {$node = $lm.lambdadef;}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? '{' el=exprlist '}'
                             {$node = optimizer.optimize(new ArrayDef($el.list));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_MAPPED)}? '{' mapped=mappedexprlist '}'
                             {$node = optimizer.optimize(new MappedDef($mapped.map));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LIBRARY_REFERENCE)}? 'java:' javafqn=javafqname ( hasarg='(' el=exprlist ')')?
                             {$node = optimizer.optimize($hasarg != null ? new JavaRef($javafqn.javafqn, Optional.<List<Node>> of($el.list), this.loader) : new JavaRef($javafqn.javafqn, this.loader));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LIBRARY_REFERENCE)}? lib=Identifier ':' entry=Identifier
                             {$node = optimizer.optimize(new LibraryRef($lib.text, $entry.text));}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FUNCTION)}? ':' Identifier '(' el=exprlist ')'
                             {$node = optimizer.optimize(new FunctionCall($Identifier.text, $el.list));}
                           | 'CASE' {Node test = null; Node otherwise = null;} (case_=expr {test = $case_.node;})?
                             {List<CaseWhen> cases = new ArrayList<>();} (when=whenspec {cases.add($when.node);})*
                             ('ELSE' else_=expr {otherwise = $else_.node; } )? 'END'
                             {$node = optimizer.optimize(new Case(test, cases, otherwise));}
;  

javafqname returns [List<String> javafqn] : {$javafqn = new ArrayList<String>();}
                                            ident1=Identifier {$javafqn.add($ident1.text);}
                                            '.' ident2=Identifier {$javafqn.add($ident2.text);}
                                            ('.' ident3=Identifier {$javafqn.add($ident3.text);} )*
;


columnref returns [FieldRef fieldref]: '@' Identifier
                                       {$fieldref = (FieldRef)optimizer.optimize(new FieldRef($Identifier.text));}
; 

lambda returns [LambdaDef lambdadef]: '('
          {List<String> parameters = new ArrayList<>();}
        Identifier
          {parameters.add($Identifier.text);}
        (',' Identifier {parameters.add($Identifier.text);} )* '->' e=expr ')'
          {$lambdadef = (LambdaDef)optimizer.optimize(new LambdaDef(parameters, $e.node));}
;

exprlist returns [List<Node> list]: {$list = new ArrayList<Node>();}
                                   (e1=expr {$list.add($e1.node);})? (',' e2=expr {$list.add($e2.node);} )*
;

mappedexprlist returns [Map<Node, Node> map]: {$map = new LinkedHashMap<Node, Node>();}
                                   (k=expr '=>' v=expr {$map.put($k.node, $v.node);})? (',' k=expr '=>' v=expr {$map.put($k.node, $v.node);} )*
;

lambdaexprlist returns [List<Node> list]: {$list = new ArrayList<Node>();}
                                         (e1=lambdaexpr {$list.add($e1.node);})? (',' e2=lambdaexpr {$list.add($e2.node);} )*
;

lambdaexpr returns [Node node]: e=expr
                                { $node = $e.node; }
                              | '+'
                                { $node = ShortOperators.getShortOperator("+"); }
                              | '-'
                                { $node = ShortOperators.getShortOperator("-"); }
                              | '*'
                                { $node = ShortOperators.getShortOperator("*"); }
                              | '/'
                                { $node = ShortOperators.getShortOperator("/"); }
                              | '%'
                                { $node = ShortOperators.getShortOperator("%"); }
                              | '&&'
                                { $node = ShortOperators.getShortOperator("&&"); }
                              | '||'
                                { $node = ShortOperators.getShortOperator("||"); }
;
