lexer grammar Ruby;

fragment ESCAPED_QUOTE
    : '\\"'
    ;

LITERAL
    : '"' (ESCAPED_QUOTE | ~('\n' | '\r'))*? '"'
    | '\'' ( ESCAPED_QUOTE | ~('\n' | '\r'))*? '\''
    ;

COMMA
    : ','
    ;

SEMICOLON
    : ';'
    ;

CRLF
    : '\r'? '\n'
    ;

REQUIRE
    : 'require'
    ;

END
    : 'end'
    ;

DEF
    : 'def'
    ;

RETURN
    : 'return'
    ;

PIR
    : 'pir'
    ;

IF
    : 'if'
    ;

ELSE
    : 'else'
    ;

ELSIF
    : 'elsif'
    ;

UNLESS
    : 'unless'
    ;

WHILE
    : 'while'
    ;

RETRY
    : 'retry'
    ;

BREAK
    : 'break'
    ;

FOR
    : 'for'
    ;

TRUE
    : 'true'
    ;

FALSE
    : 'false'
    ;

PLUS
    : '+'
    ;

MINUS
    : '-'
    ;

MUL
    : '*'
    ;

DIV
    : '/'
    ;

MOD
    : '%'
    ;

EXP
    : '**'
    ;

EQUAL
    : '=='
    ;

NOT_EQUAL
    : '!='
    ;

GREATER
    : '>'
    ;

LESS
    : '<'
    ;

LESS_EQUAL
    : '<='
    ;

GREATER_EQUAL
    : '>='
    ;

ASSIGN
    : '='
    ;

PLUS_ASSIGN
    : '+='
    ;

MINUS_ASSIGN
    : '-='
    ;

MUL_ASSIGN
    : '*='
    ;

DIV_ASSIGN
    : '/='
    ;

MOD_ASSIGN
    : '%='
    ;

EXP_ASSIGN
    : '**='
    ;

BIT_AND
    : '&'
    ;

BIT_OR
    : '|'
    ;

BIT_XOR
    : '^'
    ;

BIT_NOT
    : '~'
    ;

BIT_SHL
    : '<<'
    ;

BIT_SHR
    : '>>'
    ;

AND
    : 'and'
    | '&&'
    ;

OR
    : 'or'
    | '||'
    ;

NOT
    : 'not'
    | '!'
    ;

LEFT_RBRACKET
    : '('
    ;

RIGHT_RBRACKET
    : ')'
    ;

LEFT_SBRACKET
    : '['
    ;

RIGHT_SBRACKET
    : ']'
    ;

NIL
    : 'nil'
    ;

SL_COMMENT
    : ('#' ~('\r' | '\n')* '\r'? '\n') -> skip
    ;

ML_COMMENT
    : ('=begin' .*? '=end' '\r'? '\n') -> skip
    ;

WS
    : (' ' | '\t')+ -> skip
    ;

INT
    : [0-9]+
    ;

FLOAT
    : [0-9]* '.' [0-9]+
    ;

ID
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

ID_GLOBAL
    : '$' ID
    ;

ID_FUNCTION
    : ID [?]
    ;