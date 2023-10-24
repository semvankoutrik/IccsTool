package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;

import java.util.HashMap;


public class Checker {
    private HashMap<String, Expression> globalVariables = new HashMap<>();

    public void check(AST ast) {
        // Set variables
        ast.root.getChildren()
                .stream()
                .filter(node -> node instanceof VariableAssignment)
                .forEach(node -> {
                    var variableAssignment = (VariableAssignment) node;

                    globalVariables.put(variableAssignment.name.name, variableAssignment.expression);
                });

        // Check stylerules
        ast.root.getChildren()
                .stream()
                .filter(node -> node instanceof Stylerule)
                .forEach(node -> {
                    checkOperations(node);
                    checkDeclarations(node);
                });
    }

    public void checkOperations(ASTNode node) {
        node.getChildren().forEach(childNode -> {
            if (childNode instanceof Operation) {
                OperationChecker.checkOperation((Operation) childNode, globalVariables);
            } else {
                checkOperations(childNode);
            }
        });
    }

    public void checkDeclarations(ASTNode node) {
        node.getChildren().forEach(childNode -> {
            if (childNode instanceof Declaration) {
                DeclarationChecker.checkDeclaration((Declaration) childNode, globalVariables);
            } else {
                checkDeclarations(childNode);
            }
        });
    }

    public HashMap<String, Expression> getGlobalVariables() {
        return globalVariables;
    }

    public void setGlobalVariables(HashMap<String, Expression> globalVariables) {
        this.globalVariables = globalVariables;
    }
}
