grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

// Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

// Selectors
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

// General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

// All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
stylesheet: (variable | identifier)+;

//variable:
identifier: (CLASS_IDENT | ID_IDENT | LOWER_IDENT) properties;
variable: (CAPITAL_IDENT) ASSIGNMENT_OPERATOR (COLOR | PIXELSIZE | TRUE | FALSE) SEMICOLON;

// Properties
properties: OPEN_BRACE property+ CLOSE_BRACE;
property: (color | backgroundColor | width) SEMICOLON;
color: 'color' COLON (COLOR | CAPITAL_IDENT);
backgroundColor: 'background-color' COLON (COLOR | CAPITAL_IDENT);
width: 'width' COLON (PIXELSIZE | CAPITAL_IDENT);

