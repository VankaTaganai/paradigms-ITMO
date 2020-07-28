package expression.exceptions;

public class OverflowException extends CalculatingException {
    public OverflowException(final String operation) {
        super("Overflow as a result of " + operation);
    }
}
