package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;

import java.util.HashMap;


public class Checker {
    private HashMap<String, Expression> variables = new HashMap<>();

    public void check(AST ast) {
        // Set variables
        ast.root.getChildren()
                .stream()
                .filter(node -> node instanceof VariableAssignment)
                .forEach(node -> {
                    var variableAssignment = (VariableAssignment) node;

                    variables.put(variableAssignment.name.name, variableAssignment.expression);
                });

        // Check stylerules
        ast.root.getChildren()
                .stream()
                .filter(node -> node instanceof Stylerule)
                .forEach(node -> {
                    checkVariableReferences(node);
                    checkOperations(node);
                });
    }

    public void checkVariableReferences(ASTNode node) {
        node.getChildren().forEach(childNode -> {
            if (childNode instanceof Declaration) {
                var declaration = (Declaration) childNode;

                if (declaration.expression instanceof VariableReference) {
                    var reference = (VariableReference) declaration.expression;
                    var variable = variables.get(reference.name);
                    if (variable == null) reference.setError("Variable " + reference.name + " not found");
                }
            } else {
                checkVariableReferences(childNode);
            }
        });
    }

    public void checkOperations(ASTNode node) {
        node.getChildren().forEach(childNode -> {
            if (childNode instanceof Operation) {
                OperationChecker.checkOperation((Operation) childNode, variables);
            } else {
                checkOperations(childNode);
            }
        });
    }

    public HashMap<String, Expression> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, Expression> variables) {
        this.variables = variables;
    }
}
