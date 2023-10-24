package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.checker.errors.InvalidDeclarationValue;
import nl.han.ica.icss.checker.errors.UnknownProperty;
import nl.han.ica.icss.helpers.HANLinkedListHelper;

import java.util.HashMap;

public class DeclarationChecker {
    // TODO: Add support for variables.
    public static void check(Declaration declaration, IHANLinkedList<HashMap<String, Expression>> variables) {
        var scopeVariables = HANLinkedListHelper.scopeVariablesListToHashMap(variables);
        var value = declaration.expression;

        if (value instanceof VariableReference) {
            var reference = (VariableReference) declaration.expression;
            value = scopeVariables.get(reference.name);

            if (value == null) {
                return;
            }
        }

        switch (declaration.property.name) {
            case "color":
            case "background-color": {
                if (value instanceof ColorLiteral) break;

                if (value instanceof Literal) {
                    Literal v = (Literal) value;

                    declaration.setError(
                            InvalidDeclarationValue.create(declaration.property.name, v.toString()).getError()
                    );

                    break;
                }

                break;
            }
            case "width":
            case "height": {
                if (value instanceof PercentageLiteral || value instanceof PixelLiteral || value instanceof Operation)
                    break;

                if (value instanceof Literal) {
                    Literal v = (Literal) value;

                    declaration.setError(
                            InvalidDeclarationValue.create(declaration.property.name, v.toString()).getError()
                    );

                    break;
                }

                break;
            }
            default: {
                declaration.setError(UnknownProperty.create(declaration.property.name).getError());
            }
        }
    }
}
