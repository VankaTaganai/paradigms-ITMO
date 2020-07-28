package expression.exceptions;

public class WrongNumberOfBracketsException extends ParsingException {
    public WrongNumberOfBracketsException(final int index) {
        super("Wrong position or number of brackets by index " + index);
    }
}
