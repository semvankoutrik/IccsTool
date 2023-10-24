package nl.han.ica.icss.helpers;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.HashMap;

public class VariableHelper {
    public static Literal getValue(VariableAssignment variableAssignment) {
        var expr = variableAssignment.expression;

        if (expr instanceof PercentageLiteral) return (PercentageLiteral) expr;
        if (expr instanceof PixelLiteral) return (PixelLiteral) expr;
        if (expr instanceof ColorLiteral) return (ColorLiteral) expr;
        if (expr instanceof BoolLiteral) return (BoolLiteral) expr;
        if (expr instanceof Operation) {
            var operation = (Operation) expr;
            var lhs = operation.lhs;
            var rhs = operation.rhs;

            return new PixelLiteral("");
        }

        throw new RuntimeException(expr.getClass() + " is not a literal or an operation.");
    }

    public static <T> HashMap<String, T> scopeVariablesListToHashMap(IHANLinkedList<HashMap<String, T>> allVariables)
    {
        var map = new HashMap<String, T>();

        for(int i = 0; i < allVariables.getSize(); i++) {
            map.putAll(allVariables.get(i));
        }

        return map;
    }
}
