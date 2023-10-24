package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.helpers.VariableHelper;

import java.util.HashMap;

public class OperationEvaluator {
    public static Literal evaluate(Operation operation, IHANLinkedList<HashMap<String, Literal>> variables) {
        var scopeVariables = VariableHelper.scopeVariablesListToHashMap(variables);

        var lhs = operation.lhs;
        var rhs = operation.rhs;

        Expression leftLiteral;
        Expression rightLiteral;
        int leftValue = 0;
        int rightValue = 0;
        Class<?> literalClass;

        if (!(lhs instanceof Literal || lhs instanceof VariableReference)) {
            leftLiteral = evaluate((Operation) lhs, variables);
        } else {
            leftLiteral = lhs;
        }

        if (!(rhs instanceof Literal || rhs instanceof VariableReference)) {
            rightLiteral = evaluate((Operation) rhs, variables);
        } else {
            rightLiteral = rhs;
        }

        if(leftLiteral == null || rightLiteral == null) return null;

        // Get value
        if (rightLiteral instanceof VariableReference) {
            rightLiteral = scopeVariables.get(((VariableReference) rightLiteral).name);
        }
        rightValue = getValue(rightLiteral);

        if (leftLiteral instanceof VariableReference) {
            leftLiteral = scopeVariables.get(((VariableReference) leftLiteral).name);
        }
        leftValue = getValue(leftLiteral);

        // Get class of returning literal
        if (leftLiteral instanceof ScalarLiteral) literalClass = rightLiteral.getClass();
        else if (rightLiteral instanceof ScalarLiteral) literalClass = leftLiteral.getClass();
        else literalClass = leftLiteral.getClass();

        // Calculate
        int result;

        if (operation instanceof MultiplyOperation) {
            result = leftValue * rightValue;
        } else if (operation instanceof AddOperation) {
            result = leftValue + rightValue;
        } else if (operation instanceof SubtractOperation) {
            result = leftValue - rightValue;
        } else {
            result = 0;
        }

        if(literalClass == PixelLiteral.class) return new PixelLiteral(result);
        if(literalClass == PercentageLiteral.class) return new PercentageLiteral(result);

        return null;
    }

    private static int getValue(Expression expression) {
        if (expression instanceof ScalarLiteral) return ((ScalarLiteral) expression).value;
        if (expression instanceof PixelLiteral) return ((PixelLiteral) expression).value;
        if (expression instanceof PercentageLiteral) return ((PercentageLiteral) expression).value;

        return 0;
    }
}
