package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.HashMap;

import static nl.han.ica.icss.helpers.VariableHelper.scopeVariablesListToHashMap;

public class IfClauseEvaluator {

    public static boolean evaluate(IfClause ifClause, IHANLinkedList<HashMap<String, Literal>> allVariables) {
        var scopeVariables = scopeVariablesListToHashMap(allVariables);

        var expression = ifClause.conditionalExpression;

        boolean value;

        if(expression instanceof VariableReference) {
            var ref = (VariableReference) expression;
            value = ((BoolLiteral) scopeVariables.get(ref.name)).value;
        } else {
            var literal = (BoolLiteral) expression;

            value = literal.value;
        }

        return value;
    }
}
