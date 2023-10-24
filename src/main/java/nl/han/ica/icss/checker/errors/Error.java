package nl.han.ica.icss.checker.errors;

public abstract class Error {
    protected String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
