package expression.exceptions;

public class UnexpectedUnaryOperation extends ParsingException {
    public UnexpectedUnaryOperation(final int index, final String actual) {
        super("Expected unary operation, operation in brackets, constant, variable by index " + index + ". Actual: " + actual);
    }
}
