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
	
	public void setConfiguration(org.datagen.factory.Config<org.datagen.expr.interpreter.InterpreterParameters> configuration) {
		this.configuration = configuration;
	}
}

import ExpressionLexer;

start returns [Node node]: e=expr EOF 
                           {$node = $e.node;}
;

expr returns [Node node]: p=primary
                          {$node = $p.node;}
                        | e=expr '!'
                          {$node = new Factorial($e.node).optimize();}
                        | '+' e=expr
                          {$node = $e.node;}
                        | '-' e=expr
                          {$node = new Negation($e.node).optimize();}
                        | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LAMBDA_DEFINITION)}? l=primary '(' el=lambdaexprlist? ')'
                          {$node = new LambdaCall($l.node, $el.list).optimize();}
                        | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? a=primary '[' i=expr ']'
                          {$node = new ArrayRef($a.node, $i.node).optimize();}
                        | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? a=primary '[' i=expr '..' i2=expr ']'
                          {$node = new ArrayRangeRef($a.node, $i.node, $i2.node).optimize();}
                        | e=expr '.' Identifier
                          {$node = new AttrRef($e.node, $Identifier.text).optimize();}
                        | e1=expr '^' e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, Arithmetic.POW).optimize();}
                        | e1=expr { Arithmetic op = null; } ('*' {op = Arithmetic.MUL;} | '/' {op = Arithmetic.DIV;} | '%' {op = Arithmetic.MOD;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op).optimize();}
                        | e1=expr { Arithmetic op = null; } ('+' {op = Arithmetic.ADD;} | '-' {op = Arithmetic.SUB;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op).optimize();}
                        | e1=expr '<=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS_EQUAL).optimize();}
                        | e1=expr '>=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER_EQUAL).optimize();}
                        | e1=expr '<' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS).optimize();}
                        | e1=expr '>' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER).optimize();}
                        | e1=expr ('=' | '==') e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.EQUAL).optimize();}
                        | e1=expr ('!=' | '<>') e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.NOT_EQUAL).optimize();}
                        | ('!' | 'NOT') e=expr
                          {$node = new Not($e.node).optimize();}
                        | e1=expr ('AND' | '&&') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.AND).optimize();}
                        | e1=expr ('OR' | '||') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.OR).optimize();}
                        | e1=expr ('XOR' | '^^') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.XOR).optimize();}
                        | <assoc=right> e1=expr '?' e2=expr ':' e3=expr
                          {$node = new Ternary($e1.node, $e2.node, $e3.node).optimize();}
;

whenspec returns [CaseWhen node]: 'WHEN' when=expr 'THEN' then=expr
                                  {$node = new CaseWhen($when.node, $then.node);}
;

primary returns [Node node]: '(' e=expr ')'
                             {$node = $e.node;}
                           | 'TYPEOF' '(' e=expr ')'
                             {$node = new TypeOf($e.node).optimize();}
                           | 'EVAL' '(' e=expr ')'
                             {$node = new Eval($e.node).optimize();}
                           | 'PARALLEL' '(' '{' el=exprlist '}' { Node reducer = null; } ( ',' r=lambdaexpr { reducer = $r.node; } )? ')'
                             { $node = new Parallel(new ArrayDef($el.list), reducer); }
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LAMBDA_DEFINITION)}? l=lambda
                             {$node = $l.lambdadef;}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? '{' el=exprlist '}'
                             {$node = new ArrayDef($el.list).optimize();}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? '{' mapped=mappedexprlist '}'
                             {$node = new MappedDef($mapped.map).optimize();}
                           | IntegerConstant
                             {$node = new LiteralValue(Long.parseLong($IntegerConstant.text)).optimize();}
                           | DecimalFloatingConstant
                             {$node = new LiteralValue(Double.parseDouble($DecimalFloatingConstant.text)).optimize();}
                           | StringLiteral
                             {String string = $StringLiteral.text; $node = new LiteralValue(string == null ? "" : string.substring(1, string.length() - 1)).optimize();}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FIELD_REFERENCE)}? col=columnref
                             {$node = $col.fieldref;}
                           | Identifier
                             {$node = new VariableRef($Identifier.text).optimize();}
                           | 'this'
                             {$node = new ThisRef().optimize();}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LIBRARY_REFERENCE)}? lib=Identifier ':' entry=Identifier
                             {$node = new LibraryRef($lib.text, $entry.text).optimize();}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_PROPERTY_REFERENCE)}? '$' Identifier
                             {$node = new PropertyRef($Identifier.text).optimize();}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FUNCTION)}? ':' Identifier '(' el=exprlist ')'
                             {$node = new FunctionCall($Identifier.text, $el.list).optimize();}
                           | 'true'
                             {$node = LiteralValue.TRUE;}
                           | 'false'
                             {$node = LiteralValue.FALSE;}
                           | 'CASE' {Node test = null; Node otherwise = null;} (case_=expr {test = $case_.node;})?
                             {List<CaseWhen> cases = new ArrayList<>();} (when=whenspec {cases.add($when.node);})*
                             ('ELSE' else_=expr {otherwise = $else_.node; } )? 'END'
                             {$node = new Case(test, cases, otherwise).optimize();}
;

columnref returns [FieldRef fieldref]: '@' Identifier
                                       {$fieldref = new FieldRef($Identifier.text);}
; 

lambda returns [LambdaDef lambdadef]: '('
          {List<String> parameters = new ArrayList<>();}
        Identifier
          {parameters.add($Identifier.text);}
        (',' Identifier {parameters.add($Identifier.text);} )* '->' e=expr ')'
          {$lambdadef = new LambdaDef(parameters, $e.node);}
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
;
