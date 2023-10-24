package nl.han.ica.icss.checker.errors;

public class InvalidDeclarationValue extends Error {
    public InvalidDeclarationValue(String error) {
        super(error);
    }

    public static InvalidDeclarationValue create(String propertyName, String value) {
        return new InvalidDeclarationValue("Invalid value '" + value + "' for property " + propertyName);
    }
}
