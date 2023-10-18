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

// Values
colorValue: COLOR;
pixelValue: PIXELSIZE;
percentageValue: PERCENTAGE;
boolValue: (TRUE | FALSE);

// Variables
variableReference: CAPITAL_IDENT;
variableValue: (colorValue | pixelValue | boolValue | percentageValue);
variableAssignment: variableReference ASSIGNMENT_OPERATOR variableValue SEMICOLON;

// Properties
properties: OPEN_BRACE property+ CLOSE_BRACE;
propertyName: 'color' | 'background-color' | 'width';
propertyValue: (colorValue | pixelValue | boolValue | percentageValue | variableReference);
property: propertyName COLON propertyValue SEMICOLON;

color: 'color' COLON (colorValue | variableReference);
backgroundColor: 'background-color' COLON (colorValue | variableReference);
width: 'width' COLON (pixelValue | variableReference);

// Calculations
//multiplication: (SCALAR | multiplication) * (SCALAR | multiplication);

