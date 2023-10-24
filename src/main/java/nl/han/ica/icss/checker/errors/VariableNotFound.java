package nl.han.ica.icss.checker.errors;

public class VariableNotFound extends Error {
    public VariableNotFound(String variableName) {
        super("Variable " + variableName + " is not declared in this scope");
    }

    public static VariableNotFound create(String variableName) {
        return new VariableNotFound(variableName);
    }
}
