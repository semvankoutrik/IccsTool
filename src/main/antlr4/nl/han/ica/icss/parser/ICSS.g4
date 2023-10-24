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
stylesheet: (variableAssignment | stylerule)+;

stylerule: selector properties;
selector: (CLASS_IDENT | ID_IDENT | LOWER_IDENT);

// Calculations
operationValue: scalarValue | percentageValue | pixelValue | variableReference;
operation: add | subtract | multiply;
multiply: operationValue MUL (operationValue | operation);
add: operationValue PLUS (operationValue | operation);
subtract: operationValue MIN (operationValue | operation);

// Values
scalarValue: SCALAR;
colorValue: COLOR;
pixelValue: PIXELSIZE;
percentageValue: PERCENTAGE;
boolValue: (TRUE | FALSE);

// Variables
variableReference: CAPITAL_IDENT;
variableValue: (colorValue | scalarValue | pixelValue | boolValue | percentageValue);
variableAssignment: variableReference ASSIGNMENT_OPERATOR variableValue SEMICOLON;

// Properties
properties: OPEN_BRACE (property | ifClause | variableAssignment)* CLOSE_BRACE;
propertyName: 'color' | 'background-color' | 'width' | 'height';
propertyValue: (colorValue | pixelValue | boolValue | percentageValue | variableReference | operation);
property: propertyName COLON propertyValue SEMICOLON;

// If-else
ifClause: IF BOX_BRACKET_OPEN (boolValue | variableReference) BOX_BRACKET_CLOSE properties elseClause?;
elseClause: ELSE properties;
