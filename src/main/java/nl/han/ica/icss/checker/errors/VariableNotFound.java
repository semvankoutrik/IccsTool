package nl.han.ica.icss.checker.errors;

public class VariableNotFound extends Error {
    public VariableNotFound(String variableName) {
        super("Variable" + variableName + " not found");
    }
}
