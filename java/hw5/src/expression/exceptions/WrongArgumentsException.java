package expression.exceptions;

public class WrongArgumentsException extends CalculatingException {
    public WrongArgumentsException(final String operation) {
        super("Wrong arguments in " + operation);
    }
}
