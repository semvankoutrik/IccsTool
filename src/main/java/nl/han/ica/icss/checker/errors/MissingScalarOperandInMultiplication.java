package nl.han.ica.icss.checker.errors;

public class MissingScalarOperandInMultiplication extends Error {
    public MissingScalarOperandInMultiplication(String error) {
        super(error);
    }

    public static MissingScalarOperandInMultiplication create(String lhs, String rhs) {
        return new MissingScalarOperandInMultiplication(
                "One operand in a multiplication should be a scalar value, given: '" + lhs + "' '" + rhs + "'"
        );
    }
}
