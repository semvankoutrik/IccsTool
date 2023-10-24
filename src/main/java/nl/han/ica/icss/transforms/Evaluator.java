package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.helpers.VariableHelper;

import java.util.HashMap;

public class Evaluator implements Transform {
    private final IHANLinkedList<HashMap<String, Literal>> variables;
    private final Stylesheet stylesheet = new Stylesheet();

    public Evaluator() {
        variables = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        apply(ast.root);

        ast.setRoot(stylesheet);
    }

    public void apply(ASTNode node) {
        var scopeVariables = new HashMap<String, Literal>();
        variables.add(scopeVariables);

        node.getChildren().forEach(childNode -> {
            if (childNode instanceof VariableAssignment) {
                var assignment = (VariableAssignment) childNode;

                Literal literal = VariableHelper.getValue(assignment);

                scopeVariables.put(assignment.name.name, literal);
            }

            if (childNode instanceof Stylerule) {
                Stylerule stylerule = evaluateStylerule((Stylerule) childNode);

                stylesheet.addChild(stylerule);
            }
        });

        variables.removeLast();
    }

    public Stylerule evaluateStylerule(Stylerule stylerule) {
        var scopeVariables = new HashMap<String, Literal>();
        variables.add(scopeVariables);

        Stylerule newStylerule = new Stylerule();
        newStylerule.selectors = stylerule.selectors;

        stylerule.body.forEach(childNode -> {
            if (childNode instanceof VariableAssignment) applyVariableAssignment(scopeVariables, childNode);
            if (childNode instanceof IfClause) applyIfClause((IfClause) childNode, newStylerule);
            if (childNode instanceof Declaration) applyDeclaration((Declaration) childNode, newStylerule);
        });

        variables.removeLast();

        return newStylerule;
    }

    private void applyVariableAssignment(HashMap<String, Literal> scopeVariables, ASTNode childNode) {
        var assignment = (VariableAssignment) childNode;

        Literal literal = VariableHelper.getValue(assignment);

        var allVariables = VariableHelper.scopeVariablesListToHashMap(variables);
        if (allVariables.containsKey(assignment.name.name)) {
            var map = variables.getWhere((scope) -> scope.get(assignment.name.name) != null);

            map.put(assignment.name.name, literal);
        } else {
            scopeVariables.put(assignment.name.name, literal);
        }
    }

    public void applyStyleruleASTNode(ASTNode astNode, Stylerule stylerule) {
        var scopeVariables = new HashMap<String, Literal>();
        variables.add(scopeVariables);

        if (astNode instanceof VariableAssignment) applyVariableAssignment(scopeVariables, astNode);
        if (astNode instanceof IfClause) applyIfClause((IfClause) astNode, stylerule);
        if (astNode instanceof Declaration) applyDeclaration((Declaration) astNode, stylerule);

        variables.removeLast();
    }

    public void applyDeclaration(Declaration declaration, Stylerule stylerule) {
        var scopeVariables = VariableHelper.scopeVariablesListToHashMap(variables);

        if (declaration.expression instanceof VariableReference) {
            var ref = (VariableReference) declaration.expression;

            declaration.expression = scopeVariables.get(ref.name);
        }

        if (declaration.expression instanceof Operation) {
            var operation = (Operation) declaration.expression;

            declaration.expression = OperationEvaluator.evaluate(operation, variables);
        }

        stylerule.addChild(declaration);
    }

    public void applyIfClause(IfClause ifClause, Stylerule stylerule) {
        var scopeVariables = new HashMap<String, Literal>();
        variables.add(scopeVariables);

        boolean value = IfClauseEvaluator.evaluate(ifClause, variables);

        if (value) {
            ifClause.body.forEach(childNode -> {
                applyStyleruleASTNode(childNode, stylerule);
            });
        } else {
            if (ifClause.elseClause != null) {
                ifClause.elseClause.body.forEach(childNode -> {
                    applyStyleruleASTNode(childNode, stylerule);
                });
            }
        }

        variables.removeLast();
    }
}
