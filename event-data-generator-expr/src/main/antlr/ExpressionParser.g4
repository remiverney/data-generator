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
	
	public void setConfiguration(org.datagen.factory.Config<org.datagen.expr.interpreter.InterpreterParameters> configuration) {
		this.configuration = configuration;
	}
	
	public void setEvalContext(org.datagen.expr.ast.context.EvalContext context) {
		this.context = context;
	}
}

import ExpressionLexer;

start returns [Node node]: e=expr EOF 
                           {$node = $e.node;}
;

expr returns [Node node]: p=primary
                          {$node = $p.node;}
                        | e=expr '.' Identifier
                          {$node = new AttrRef($e.node, $Identifier.text).optimize(context);}
                        | l=expr '(' el=lambdaexprlist ')'
                          {$node = new LambdaCall($l.node, $el.list).optimize(context);}
                        | a=expr '[' i=expr ']'
                          {$node = new ArrayRef($a.node, $i.node).optimize(context);}
                        | a=expr '[' i=expr '..' i2=expr ']'
                          {$node = new ArrayRangeRef($a.node, $i.node, $i2.node).optimize(context);}
                         | e=expr '!'
                          {$node = new Factorial($e.node).optimize(context);}
                        | '+' e=expr
                          {$node = $e.node;}
                        | '-' e=expr
                          {$node = new Negation($e.node).optimize(context);}
                        | e1=expr '^' e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, Arithmetic.POW).optimize(context);}
                        | e1=expr { Arithmetic op = null; } ('*' {op = Arithmetic.MUL;} | '/' {op = Arithmetic.DIV;} | '%' {op = Arithmetic.MOD;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op).optimize(context);}
                        | e1=expr { Arithmetic op = null; } ('+' {op = Arithmetic.ADD;} | '-' {op = Arithmetic.SUB;}) e2=expr
                          {$node = new ArithmeticOp($e1.node, $e2.node, op).optimize(context);}
                        | e1=expr '<=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS_EQUAL).optimize(context);}
                        | e1=expr '>=' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER_EQUAL).optimize(context);}
                        | e1=expr '<' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.LESS).optimize(context);}
                        | e1=expr '>' e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.GREATER).optimize(context);}
                        | e1=expr ('=' | '==') e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.EQUAL).optimize(context);}
                        | e1=expr ('!=' | '<>') e2=expr
                          {$node = new ComparisonOp($e1.node, $e2.node, Comparison.NOT_EQUAL).optimize(context);}
                        | ('!' | 'NOT') e=expr
                          {$node = new Not($e.node).optimize(context);}
                        | e1=expr ('AND' | '&&') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.AND).optimize(context);}
                        | e1=expr ('OR' | '||') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.OR).optimize(context);}
                        | e1=expr ('XOR' | '^^') e2=expr
                          {$node = new LogicOp($e1.node, $e2.node, Logic.XOR).optimize(context);}
                        | <assoc=right> e1=expr '?' e2=expr ':' e3=expr
                          {$node = new Ternary($e1.node, $e2.node, $e3.node).optimize(context);}
;

whenspec returns [CaseWhen node]: 'WHEN' when=expr 'THEN' then=expr
                                  {$node = new CaseWhen($when.node, $then.node).optimize(context);}
;

primary returns [Node node]: '(' e=expr ')'
                             {$node = $e.node;}
                           | IntegerConstant
                             {$node = new LiteralValue(Long.parseLong($IntegerConstant.text)).optimize(context);}
                           | DecimalFloatingConstant
                             {$node = new LiteralValue(Double.parseDouble($DecimalFloatingConstant.text)).optimize(context);}
                           | StringLiteral
                             {String string = $StringLiteral.text; $node = new LiteralValue(string == null ? "" : string.substring(1, string.length() - 1)).optimize(context);}
                           | Identifier
                             {$node = new VariableRef($Identifier.text).optimize(context);}
                           | 'this'
                             {$node = new ThisRef().optimize(context);}
                           | 'true'
                             {$node = LiteralValue.TRUE;}
                           | 'false'
                             {$node = LiteralValue.FALSE;}
                           | 'TYPEOF' '(' e=expr ')'
                             {$node = new TypeOf($e.node).optimize(context);}
                           | 'EVAL' '(' e=expr ')'
                             {$node = new Eval($e.node).optimize(context);}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_PROPERTY_REFERENCE)}? '$' Identifier
                             {$node = new PropertyRef($Identifier.text).optimize(context);}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FIELD_REFERENCE)}? col=columnref
                             {$node = $col.fieldref;}
                           | 'PARALLEL' '(' '{' el=exprlist '}' { Node reducer = null; } ( ',' r=lambdaexpr { reducer = $r.node; } )? ')'
                             { $node = new Parallel(new ArrayDef($el.list).optimize(context), reducer).optimize(context); }
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LAMBDA_DEFINITION)}? lm=lambda
                             {$node = $lm.lambdadef;}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_ARRAY)}? '{' el=exprlist '}'
                             {$node = new ArrayDef($el.list).optimize(context);}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_MAPPED)}? '{' mapped=mappedexprlist '}'
                             {$node = new MappedDef($mapped.map).optimize(context);}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_LIBRARY_REFERENCE)}? lib=Identifier ':' entry=Identifier
                             {$node = new LibraryRef($lib.text, $entry.text).optimize(context);}
                           | {configuration.isEnabled(org.datagen.expr.interpreter.InterpreterParameters.ALLOW_FUNCTION)}? ':' Identifier '(' el=exprlist ')'
                             {$node = new FunctionCall($Identifier.text, $el.list).optimize(context);}
                           | 'CASE' {Node test = null; Node otherwise = null;} (case_=expr {test = $case_.node;})?
                             {List<CaseWhen> cases = new ArrayList<>();} (when=whenspec {cases.add($when.node);})*
                             ('ELSE' else_=expr {otherwise = $else_.node; } )? 'END'
                             {$node = new Case(test, cases, otherwise).optimize(context);}
;  

columnref returns [FieldRef fieldref]: '@' Identifier
                                       {$fieldref = new FieldRef($Identifier.text);}
; 

lambda returns [LambdaDef lambdadef]: '('
          {List<String> parameters = new ArrayList<>();}
        Identifier
          {parameters.add($Identifier.text);}
        (',' Identifier {parameters.add($Identifier.text);} )* '->' e=expr ')'
          {$lambdadef = new LambdaDef(parameters, $e.node).optimize(context);}
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
