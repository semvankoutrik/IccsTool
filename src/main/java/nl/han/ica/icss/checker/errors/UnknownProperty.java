package nl.han.ica.icss.checker.errors;

public class UnknownProperty extends Error {
    public UnknownProperty(String error) {
        super(error);
    }

    public static UnknownProperty create(String property) {
        return new UnknownProperty("Property " + property + " unknown");
    }
}
