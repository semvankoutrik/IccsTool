package nl.han.ica.icss.checker.errors;

public class InvalidConditionalExpression extends Error {
    public InvalidConditionalExpression(String error) {
        super(error);
    }

    public static InvalidConditionalExpression create(String value) {
        return new InvalidConditionalExpression("Value " + value + " is not a conditional expression");
    }
}
