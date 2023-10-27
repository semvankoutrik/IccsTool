package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.checker.errors.InvalidConditionalExpression;
import nl.han.ica.icss.checker.errors.VariableNotFound;
import nl.han.ica.icss.helpers.VariableHelper;

import java.util.HashMap;

public class IfClauseChecker {
    public static void check(IfClause ifClause, IHANLinkedList<HashMap<String, Expression>> variables) {
        var scopeVariables = VariableHelper.scopeVariablesListToHashMap(variables);
        var expr = ifClause.conditionalExpression;

        if (expr instanceof VariableReference) {
            var ref = (VariableReference) expr;

            var value = scopeVariables.get(ref.name);

            if (value == null) {
                ifClause.setError(VariableNotFound.create(ref.name).getError());
            } else {
                if (!(value instanceof BoolLiteral)) {
                    ifClause.setError(InvalidConditionalExpression.create(value.toString()).getError());
                }
            }
        } else {
            // Not allowed by the parser as well, but just to be sure.
            if (!(expr instanceof BoolLiteral)) {
                ifClause.setError(InvalidConditionalExpression.create(expr.toString()).getError());
            }
        }

        Checker.check(ifClause.getChildren(), variables);

        var elseClause = ifClause.elseClause;

        if (elseClause != null) {
            Checker.check(elseClause.getChildren(), variables);
        }
    }
}
