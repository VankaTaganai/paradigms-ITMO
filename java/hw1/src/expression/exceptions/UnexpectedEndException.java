package expression.exceptions;

public class UnexpectedEndException extends ParsingException {
    public UnexpectedEndException(final int index, final String actual) {
        super("Expected end or operation of expression by index " + index + ". Actual: " + actual);
    }
}
