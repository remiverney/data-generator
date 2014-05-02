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
                          {$node = new Factorial($e.node);}
                        | '+' e=expr
                          {$node = $e.node;}
                        | '-' e=expr
                          {$node = new Negation($e.node);}
                        | l=expr '(' el=lambdaexprlist? ')'
                          {$node = new LambdaCall($l.node, $el.list);}
                        | a=expr '[' i=expr ']'
                          {$node = new ArrayRef($a.node, $i.node);}
                        | a=expr '[' i=expr '..' i2=expr ']'
                          {$node = new ArrayRangeRef($a.node, $i.node, $i2.node);}
                        | e=expr '.' Identifier
                          {$node = new AttrRef($e.node, $Identifier.text);}
                        | e1=expr '^' e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, Arithmetic.POW);}
                        | e1=expr { Arithmetic op = null; } ('*' {op = Arithmetic.MUL;} | '/' {op = Arithmetic.DIV;} | '%' {op = Arithmetic.MOD;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op);}
                        | e1=expr { Arithmetic op = null; } ('+' {op = Arithmetic.ADD;} | '-' {op = Arithmetic.SUB;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op);}
                        | e1=expr '<=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS_EQUAL);}
                        | e1=expr '>=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER_EQUAL);}
                        | e1=expr '<' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS);}
                        | e1=expr '>' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER);}
                        | e1=expr '=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.EQUAL);}
                        | e1=expr '==' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.EQUAL);}
                        | e1=expr '!=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.NOT_EQUAL);}
                        | e1=expr '<>' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.NOT_EQUAL);}
                        | ('!' | 'NOT') e=expr
                          {$node = new Not($e.node);}
                        | e1=expr ('AND' | '&&') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.AND);}
                        | e1=expr ('OR' | '||') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.OR);}
                        | e1=expr ('XOR' | '^^') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.XOR);}
                        | <assoc=right> e1=expr '?' e2=expr ':' e3=expr
                          {$node = new Ternary($e1.node, $e2.node, $e3.node);}
                        | 'CASE' {Node test = null; Node otherwise = null;} (case_=expr {test = $case_.node;})?
                          {List<CaseWhen> cases = new ArrayList<>();} (when=whenspec {cases.add($when.node);})*
                          ('ELSE' else_=expr {otherwise = $else_.node; } )? 'END'
                          {$node = new Case(test, cases, otherwise);}
;

whenspec returns [CaseWhen node]: 'WHEN' when=expr 'THEN' then=expr
                                  {$node = new CaseWhen($when.node, $then.node);}
;

primary returns [Node node]: '(' e=expr ')'
                             {$node = $e.node;}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LAMBDA_DEFINITION)}? l=lambda
                             {$node = $l.lambdadef;}
                           | '{' el=exprlist '}'
                             {$node = new ArrayDef($el.list);}
                           | IntegerConstant
                             {$node = new LiteralValue(Long.parseLong($IntegerConstant.text));}
                           | DecimalFloatingConstant
                             {$node = new LiteralValue(Double.parseDouble($DecimalFloatingConstant.text));}
                           | StringLiteral
                             {$node = new LiteralValue($StringLiteral.text.substring(1, $StringLiteral.text.length() - 1));}
                           | col=columnref
                             {$node = $col.fieldref;}
                           | Identifier
                             {$node = new VariableRef($Identifier.text);}
                           | 'this'
                             {$node = new ThisRef();}
                           | lib=Identifier ':' entry=Identifier
                             {$node = new LibraryRef($lib.text, $entry.text);}
                           | '$' Identifier
                             {$node = new PropertyRef($Identifier.text);}
                           | ':' Identifier '(' el=exprlist ')'
                             {$node = new FunctionCall($Identifier.text, $el.list);}
                           | 'true'
                             {$node = LiteralValue.TRUE;}
                           | 'false'
                             {$node = LiteralValue.FALSE;}
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
