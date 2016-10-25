%{

%}
%%
D = [0-9]
L = [A-Z]|[a-z]|[_$]
identifier = {L}
constantExpression  = {expression} 
expression  = {assignmentExpression} 
assignmentExpression  = {conditionalExpression} | {assignment} 
assignment  = {leftHandSide} {assignmentOperator} {assignmentExpression} 
leftHandSide  = {expressionName} | {fieldAccess} | {arrayAccess} 
assignmentOperator  = "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | } } "=" | } } } "=" | "&=" | "^=" | "|=" 
conditionalExpression  = {conditionalOrExpression} | {conditionalOrExpression} "?" {expression} ":" {conditionalExpression} 
conditionalOrExpression  = {conditionalAndExpression} | {conditionalOrExpression} "||" {conditionalAndExpression} 
conditionalAndExpression  = {inclusiveOrExpression} | {conditionalAndExpression} "&&" {inclusiveOrExpression} 
inclusiveOrExpression  = {exclusiveOrExpression} | {inclusiveOrExpression} | {exclusiveOrExpression} 
exclusiveOrExpression  = {andExpression} | {exclusiveOrExpression} "^" {andExpression} 
andExpression  = {equalityExpression} | {andExpression} & {equalityExpression} 
equalityExpression  = {relationalExpression} | {equalityExpression} "==" {relationalExpression} | {equalityExpression} "!=" {relationalExpression} 
relationalExpression  = {shiftExpression} | {relationalExpression} ({shiftExpression} | {relationalExpression} ) {shiftExpression} | {relationalExpression} "<=" {shiftExpression} | {relationalExpression} } "=" {shiftExpression} | {relationalExpression} instanceof {referenceType} 
shiftExpression  = {additiveExpression} | {shiftExpression} ( "<<" {additiveExpression} | {shiftExpression} } ) {additiveExpression} | {shiftExpression} } } } {additiveExpression} 
additiveExpression  = {multiplicativeExpression} | {additiveExpression} "+" {multiplicativeExpression} | {additiveExpression} "-" {multiplicativeExpression} 
multiplicativeExpression  = {unaryExpression} | {multiplicativeExpression} "*" {unaryExpression} | {multiplicativeExpression} "/" {unaryExpression} | {multiplicativeExpression} % {unaryExpression} 
castExpression  = "(" {primitiveType} ")" {unaryExpression} | "(" {referenceType} ")" {unaryExpressionNotPlusMinus} 
unaryExpression  = {preincrementExpression} | {predecrementExpression} | "+" {unaryExpression} | "-" {unaryExpression} | {unaryExpressionNotPlusMinus} 
predecrementExpression  = "--" {unaryExpression} 
preincrementExpression  = "++" {unaryExpression} 
unaryExpressionNotPlusMinus  = {postfixExpression} | "~" {unaryExpression} | ! {unaryExpression} | {castExpression} 
postdecrementExpression  = {postfixExpression} "--" 
postincrementExpression  = {postfixExpression} "++"
postfixExpression  = {primary} | {expressionName} | {postincrementExpression} | {postdecrementExpression} 
methodInvocation  = {methodName} "(" {argumentList} ? ")" | {primary} "." {identifier} "(" {argumentList} ? ")" | super . {identifier} "(" {argumentList} ? ")" 
fieldAccess  = {primary} . {identifier} | super . {identifier} 
primary  = {primaryNoNewArray} | {arrayCreationExpression} 
primaryNoNewArray  = {literal} | "this" | "(" {expression} ")" | {classInstanceCreationExpression} | {fieldAccess} | {methodInvocation} | {arrayAccess} 
classInstanceCreationExpression  = "new" {classType} "(" {argumentList} ? ")" 
argumentList  = {expression} | {argumentList} , {expression} 
arrayCreationExpression  = new {primitiveType} {dimExprs} {dims} ? | "new" {classOrInterfaceType} {dimExprs} {dims} ? 
dimExprs  = {dimExpr} | {dimExprs} {dimExpr} 
dimExpr  = "[" {expression} "]" 
dims  = "[" "]" | {dims} "[" "]" 
arrayAccess  = {expressionName} "[" {expression} "]" | {primaryNoNewArray} "[" {expression} "]" 


packageName  = identifier | packageName . identifier 
typeName  = identifier | packageName . identifier 
simpleTypeName  = identifier 
expressionName  = identifier | ambiguousName . identifier 
methodName  = identifier | ambiguousName . identifier 
ambiguousName  = identifier | ambiguousName . identifier 
literal  = integerLiteral | floatingPointLiteral | booleanLiteral | characterLiteral | stringLiteral | nullLiteral 
integerLiteral  = decimalIntegerLiteral | hexIntegerLiteral | octalIntegerLiteral 
decimalIntegerLiteral  = decimalNumeral integerTypeSuffix ? 
hexIntegerLiteral  = hexNumeral integerTypeSuffix ? 
octalIntegerLiteral  = octalNumeral integerTypeSuffix ? 
integerTypeSuffix  = l | L 
decimalNumeral  = 0 | nonZeroDigit digits ? 
digits  = digit | digits digit 
digit  = 0 | nonZeroDigit 
nonZeroDigit  = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
hexNumeral  = 0 x hexDigit | 0 X hexDigit | hexNumeral hexDigit 
hexDigit  = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | a | b | c | d | e | f | A | B | C | D | E | F 
octalNumeral  = 0 octalDigit | octalNumeral octalDigit 
octalDigit  = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 
floatingPointLiteral  = digits . digits ? exponentPart ? floatTypeSuffix ? 
digits  = exponentPart ? floatTypeSuffix ? 
exponentPart  = exponentIndicator signedInteger 
exponentIndicator  = e | E 
signedInteger  = sign ? digits 
sign  = "+" | - 
floatTypeSuffix  = f | F | d | D 
booleanLiteral  = true | false 
characterLiteral  = ' singleCharacter ' | ' escapeSequence ' 
singleCharacter  = inputCharacter except ' and \ 
stringLiteral  = "\"" stringCharacters ? "\"" 
stringCharacters  = stringCharacter | stringCharacters stringCharacter 
stringCharacter  = inputCharacter except "\"" and \ | escapeCharacter 
nullLiteral  = null 

type  = primitiveType | referenceType 
primitiveType  = numericType | boolean 
numericType  = integralType | floatingPointType 
integralType  = byte | short | int | long | char 
floatingPointType  = float | double 
referenceType  = classOrInterfaceType | arrayType 
classOrInterfaceType  = classType | interfaceType 
classType  = typeName 
interfaceType  = typeName 
arrayType  = type "[" "]" 
keyword = abstract | boolean | break | byte | case | catch | char | class | const | continue | default | do | double | else | extends | final | finally | float | for | goto | if | implements | import | instanceof | int | interface | long | native | new | package | private | protected | public | return | short | static | super | switch | synchronized | this | throw | throws | transient | try | void | volatile | while

identifier = "'"

%%
<YYINITIAL>(({expression})) {return true;}
[^] { throw new Error("Illegal character <"+
yytext()+">"); }