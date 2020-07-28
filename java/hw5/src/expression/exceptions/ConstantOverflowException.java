package expression.exceptions;

public class ConstantOverflowException extends ParsingException {
    public ConstantOverflowException(final int index, final String actual) {
        super("Constant overflow by index " + index + ". Actual: " + actual);
    }
}
