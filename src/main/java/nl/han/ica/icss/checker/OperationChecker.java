package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.checker.errors.MissingScalarOperandInMultiplication;
import nl.han.ica.icss.checker.errors.OperandsNotCompatible;
import nl.han.ica.icss.checker.errors.VariableNotFound;
import nl.han.ica.icss.helpers.HANLinkedListHelper;

import java.util.HashMap;

public class OperationChecker {
    // TODO: Add resulting rhs of sub-operation
    public static void checkOperation(Operation operation, IHANLinkedList<HashMap<String, Expression>> variables) {
        var scopeVariables = HANLinkedListHelper.scopeVariablesListToHashMap(variables);
        Literal lhs;
        Class<?> lhsClass;

        Literal rhs;
        Class<?> rhsClass;

        if (operation.lhs instanceof VariableReference) {
            var variableReference = (VariableReference) operation.lhs;

            var expr = scopeVariables.get(variableReference.name);

            if (expr == null) {
                variableReference.setError(VariableNotFound.create(variableReference.name).getError());

                return;
            } else {
                lhs = (Literal) expr;
                lhsClass = expr.getClass();
            }
        } else {
            lhs = (Literal) operation.lhs;
            lhsClass = lhs.getClass();
        }

        if (operation.rhs instanceof VariableReference) {
            var variableReference = (VariableReference) operation.rhs;

            var expr = scopeVariables.get(variableReference.name);

            if (expr == null) {
                variableReference.setError(VariableNotFound.create(variableReference.name).getError());

                return;
            } else {
                rhs = (Literal) expr;
                rhsClass = expr.getClass();
            }
        } else {
            rhs = (Literal) operation.rhs;
            rhsClass = rhs.getClass();
        }

        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            if (lhsClass != rhsClass) {
                operation.setError(OperandsNotCompatible.create(lhs.toString(), rhs.toString()).getError());
            }
        }

        if (operation instanceof MultiplyOperation) {
            if (!(lhs instanceof ScalarLiteral) && !(rhs instanceof ScalarLiteral)) {
                operation.setError(MissingScalarOperandInMultiplication.create(lhs.toString(), rhs.toString()).getError());
            }
        }
    }
}
