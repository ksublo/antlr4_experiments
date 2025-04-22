grammar KSU_lang;

prog: stat* EOF;

stat
    : ';'                                       // empty statement
    | type idList ';'                           // declaration
    | expr ';'                                  // expression
    | 'read' idList ';'                         // read
    | 'write' exprList ';'                      // write
    | '{' stat* '}'                             // block
    | 'if' '(' expr ')' stat ('else' stat)?     // if/else
    | 'while' '(' expr ')' stat                 // while
    | 'for' '(' expr? ';' expr? ';' expr? ')' stat
    ;

type: 'int' | 'float' | 'bool' | 'string';

idList: ID (',' ID)*;

exprList: expr (',' expr)*;

expr
    : expr '.' expr                # StrConcat
    | expr op=('*'|'/') expr       # MulDiv
    | expr '%' expr                # Mod
    | expr op=('+'|'-') expr       # AddSub
    | expr op=('<'|'>'|'=='|'!=') expr # Relational
    | expr op='&&' expr            # AndExpr
    | expr op='||' expr            # OrExpr
    | '!' expr                     # NotExpr
    | '-' expr                     # Uminus
    | ID '=' expr                  # Assign
    | ID                           # Var
    | literal                      # LiteralExpr
    | '(' expr ')'                 # Parens
    ;

literal
    : INT
    | FLOAT
    | BOOL
    | STRING
    ;

BOOL: 'true' | 'false';
ID: [a-zA-Z][a-zA-Z0-9_]*;

INT
  : '0x' [0-9a-fA-F]+      // hexadecimal
  | '0' [0-7]+             // octal
  | [1-9][0-9]*            // decimal
  | '0'                    // single zero
  ;

FLOAT: [0-9]+ '.' [0-9]+;
STRING: '"' (~["\r\n])* '"';

COMMENT: '//' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;