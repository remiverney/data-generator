lexer grammar ExpressionLexer;


Whitespace : [ \t]+
	-> skip
;

Newline : ( '\r' '\n'? | '\n' )
	-> skip
;

BlockComment : '/*' .*? '*/'
	-> skip
;

LineComment : '//' ~[\r\n]*
	-> skip
;

Identifier : IdentifierNondigit ( IdentifierNondigit | Digit )*
;

IntegerConstant : DecimalConstant
                | OctalConstant
                | HexadecimalConstant
;

DecimalFloatingConstant : FractionalConstant ExponentPart?
                        | DigitSequence ExponentPart
;

StringLiteral : '"' SCharSequence? '"'
;

fragment IdentifierNondigit : Nondigit
;

fragment Nondigit : [a-zA-Z_]
;

fragment Digit : [0-9]
;

fragment DecimalConstant : NonzeroDigit Digit*
;

fragment OctalConstant : '0' OctalDigit*
;

fragment HexadecimalConstant : HexadecimalPrefix HexadecimalDigit+
;

fragment HexadecimalPrefix : '0' [xX]
;

fragment NonzeroDigit : [1-9]
;

fragment OctalDigit : [0-7]
;

fragment HexadecimalDigit : [0-9a-fA-F]
;


fragment FractionalConstant : DigitSequence? '.' DigitSequence
                            | DigitSequence '.'
;

fragment ExponentPart : 'e' Sign? DigitSequence
                      | 'E' Sign? DigitSequence
;

fragment Sign : '+' | '-'
;

fragment DigitSequence : Digit+
;

fragment SCharSequence : SChar+
;

fragment SChar : ~["\\\r\n] | EscapeSequence
;

fragment EscapeSequence : SimpleEscapeSequence
                        | OctalEscapeSequence
                        | HexadecimalEscapeSequence
;

fragment SimpleEscapeSequence : '\\' ['"?abfnrtv\\]
;

fragment OctalEscapeSequence : '\\' OctalDigit
                             | '\\' OctalDigit OctalDigit
                             | '\\' OctalDigit OctalDigit OctalDigit
;

fragment HexadecimalEscapeSequence : '\\x' HexadecimalDigit+
;

