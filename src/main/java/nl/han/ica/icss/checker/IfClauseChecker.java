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
    public static void check(IfClause node, IHANLinkedList<HashMap<String, Expression>> variables) {
        var scopeVariables = VariableHelper.scopeVariablesListToHashMap(variables);
        var expr = node.conditionalExpression;

        if (expr instanceof VariableReference) {
            var ref = (VariableReference) expr;

            var value = scopeVariables.get(ref.name);

            if (value == null) {
                node.setError(VariableNotFound.create(ref.name).getError());
            } else {
                if (!(value instanceof BoolLiteral)) {
                    node.setError(InvalidConditionalExpression.create(value.toString()).getError());
                }
            }
        } else {
            // Not allowed by the parser as well, but just to be sure.
            if (!(expr instanceof BoolLiteral)) {
                node.setError(InvalidConditionalExpression.create(expr.toString()).getError());
            }
        }

        Checker.check(node.getChildren(), variables);

        var elseClause = node.elseClause;

        if (elseClause != null) {
            Checker.check(elseClause.getChildren(), variables);
        }
    }
}
