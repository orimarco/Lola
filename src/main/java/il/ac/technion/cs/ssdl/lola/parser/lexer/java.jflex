constantExpression  = {expression} 
expression  = {assignmentExpression} 
assignmentExpression  = {conditionalExpression} | {assignment} 
assignment  = {leftHandSide} {assignmentOperator} {assignmentExpression} 
leftHandSide  = {expressionName} | {fieldAccess} | {arrayAccess} 
assignmentOperator  = "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | } } "=" | } } } "=" | "&=" | "^=" | "|=" 
conditionalExpression  = {conditionalOrExpression} | {conditionalOrExpression} "?" {expression} ":" {conditionalExpression} 
conditionalOrExpression  = {conditionalAndExpression} | {conditionalOrExpression} || {conditionalAndExpression} 
conditionalAndExpression  = {inclusiveOrExpression} | {conditionalAndExpression} && {inclusiveOrExpression} 
inclusiveOrExpression  = {exclusiveOrExpression} | {inclusiveOrExpression} | {exclusiveOrExpression} 
exclusiveOrExpression  = {andExpression} | {exclusiveOrExpression} ^ {andExpression} 
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
