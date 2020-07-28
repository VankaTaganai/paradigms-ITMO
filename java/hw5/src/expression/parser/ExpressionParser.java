package expression.parser;

import expression.*;
import expression.evaluate.Operation;
import expression.exceptions.*;

public class ExpressionParser<T> {
    private Source source;
    private char symbol;
    private final Operation<T> operation;

    private static int MAX_PRIORITY = 3;

    public ExpressionParser(Operation<T> operation) {
        this.operation = operation;
    }

    public CommonExpression<T> parse(String expression) throws ParsingException {
        source = new Source(expression);
        nextChar();
        CommonExpression<T> result = parseExpression(0);
        if (symbol != '\0') {
            throw new UnexpectedEndException(source.getPos(), source.getSuffix(0));
        }
        return result;
    }

    private void nextChar() {
        symbol = source.hasNext() ? source.next() : '\0';
    }

    private Const<T> getNumber(String sign) throws ConstantOverflowException {
        StringBuilder number = new StringBuilder(sign);
        while (Character.isDigit(symbol)) {
            number.append(symbol);
            nextChar();
        }

        try {
            return new Const<T>(operation.parse(number.toString()));
        } catch (NumberFormatException e) {
            int shift = number.length() - 1;
            if (sign.equals("+")) {
                shift--;
            }
            throw new ConstantOverflowException(source.getPos(), source.getSuffix(shift));
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(symbol)) {
            nextChar();
        }
    }

    private boolean checkOperation(String operation) {
        skipWhitespace();
        boolean result = source.checkEntry(operation);
        if (result) {
            nextChar();
        }
        return result;
    }

    private CommonExpression<T> parseExpression(int priority) throws ParsingException {
        if (priority == MAX_PRIORITY) {
            return parsePrimary();
        }

        CommonExpression<T> expression = parseExpression(priority + 1);
        while (true) {
            if (priority == 0 && checkOperation("min")) {
                expression = new Min<T>(expression, parseExpression(priority + 1), operation);
            } else if (priority == 0 && checkOperation("max")) {
                expression = new Max<T>(expression, parseExpression(priority + 1), operation);
            } else if (priority == 1 && checkOperation("+")) {
                expression = new CheckedAdd<T>(expression, parseExpression(priority + 1), operation);
            } else if (priority == 1 && checkOperation("-")) {
                expression = new CheckedSubtract<T>(expression, parseExpression(priority + 1), operation);
            } else if (priority == 2 && checkOperation("*")) {
                expression = new CheckedMultiply<T>(expression, parseExpression(priority + 1), operation);
            } else if (priority == 2 && checkOperation("/")) {
                expression = new CheckedDivide<T>(expression, parseExpression(priority + 1), operation);
            } else {
                break;
            }
        }
        return expression;
    }

    private String nextToken() {
        StringBuilder token = new StringBuilder();
        while (Character.isLetterOrDigit(symbol)) {
            token.append(symbol);
            nextChar();
        }
        return token.toString();
    }

    private CommonExpression<T> parsePrimary() throws ParsingException {
        skipWhitespace();
        if (symbol == '\0') {
            throw new UnexpectedUnaryOperation(source.getPos(), source.substringWithOffset(0));
        }


        if (source.test('-')) {
            nextChar();
            if (Character.isDigit(symbol)) {
                return getNumber("-");
            } else {
                return new CheckedNegate<T>(parsePrimary(), operation);
            }
        } else if (Character.isDigit(symbol)) {
            return getNumber("+");
        } else if (source.test('(')) {
            nextChar();
            CommonExpression<T> expression = parseExpression(0);
            if (!source.test(')')) {
                throw new WrongNumberOfBracketsException(source.getPos());
            }
            nextChar();
            return expression;
        } else if (source.test('x') || source.test('y') || source.test('z')) {
            CommonExpression<T> variable = new Variable<T>(Character.toString(symbol));
            nextChar();
            return variable;
        } else {
            String token = nextToken();
            if (token.equals("count")) {
                return new Count<T>(parsePrimary(), operation);
            }
            throw new UnexpectedUnaryOperation(source.getPos(), source.substringWithOffset(token.length() - 1));
        }
    }
}
