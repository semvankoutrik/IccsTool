package nl.han.ica.icss.checker.errors;

public class OperandsNotCompatible extends Error {
    public OperandsNotCompatible(String lhs, String rhs) {
        super("Operands " + lhs + " and " + rhs + " are not compatible");
    }

    public static OperandsNotCompatible create(String lhs, String rhs) {
        return new OperandsNotCompatible(lhs, rhs);
    }
}
