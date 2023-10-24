package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;

import java.util.HashMap;


public class Checker {
    private final IHANLinkedList<HashMap<String, Expression>> variables = new HANLinkedList<>();

    public void check(AST ast) {
        check(ast.root, variables);
    }

    private void check(ASTNode astNode, IHANLinkedList<HashMap<String, Expression>> variables) {
        var scopeVariables = new HashMap<String, Expression>();
        variables.add(scopeVariables);

        astNode.getChildren().forEach(childNode -> {
            if (childNode instanceof VariableAssignment) {
                var variableAssignment = (VariableAssignment) childNode;

                scopeVariables.put(variableAssignment.name.name, variableAssignment.expression);
            }

            if (childNode instanceof Declaration) {
                DeclarationChecker.check((Declaration) childNode, variables);
            } else if (childNode instanceof Operation) {
                OperationChecker.checkOperation((Operation) childNode, variables);
            }

            check(childNode, variables);
        });

        variables.removeLast();
    }
}
