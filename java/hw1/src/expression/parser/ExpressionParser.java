package expression.parser;

import expression.*;
import expression.exceptions.*;

public class ExpressionParser implements Parser {
    private Source source;
    private char symbol;

    private static int MAX_PRIORITY = 3;

    public CommonExpression parse(String expression) throws ParsingException {
        source = new Source(expression);
        nextChar();
        CommonExpression result = parseExpression(0);
        if (symbol != '\0') {
            throw new UnexpectedEndException(source.getPos(), source.getSuffix(0));
        }
        return result;
    }

    private void nextChar() {
        symbol = source.hasNext() ? source.next() : '\0';
    }

    private Const getNumber(String sign) throws ConstantOverflowException {
        StringBuilder number = new StringBuilder(sign);
        while (Character.isDigit(symbol)) {
            number.append(symbol);
            nextChar();
        }

        try {
            return new Const(Integer.parseInt(number.toString()));
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

    private CommonExpression parseExpression(int priority) throws ParsingException {
        if (priority == MAX_PRIORITY) {
            return parsePrimary();
        }

        CommonExpression expression = parseExpression(priority + 1);
        while (true) {
            if (priority == 0 && checkOperation("+")) {
                expression = new CheckedAdd(expression, parseExpression(priority + 1));
            } else if (priority == 0 && checkOperation("-")) {
                expression = new CheckedSubtract(expression, parseExpression(priority + 1));
            } else if (priority == 1 && checkOperation("*")) {
                expression = new CheckedMultiply(expression, parseExpression(priority + 1));
            } else if (priority == 1 && checkOperation("/")) {
                expression = new CheckedDivide(expression, parseExpression(priority + 1));
            } else if (priority == 2 && checkOperation("**")) {
                expression = new CheckedPow(expression, parseExpression(priority + 1));;
            } else if (priority == 2 && checkOperation("//")) {
                expression = new CheckedLog(expression, parseExpression(priority + 1));;
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

    private CommonExpression parsePrimary() throws ParsingException {
        skipWhitespace();
        if (symbol == '\0') {
            throw new UnexpectedUnaryOperation(source.getPos(), source.substringWithOffset(0));
        }


        if (source.test('-')) {
            nextChar();
            if (Character.isDigit(symbol)) {
                return getNumber("-");
            } else {
                return new CheckedNegate(parsePrimary());
            }
        } else if (Character.isDigit(symbol)) {
            return getNumber("+");
        } else if (source.test('(')) {
            nextChar();
            CommonExpression expression = parseExpression(0);
            if (!source.test(')')) {
                throw new WrongNumberOfBracketsException(source.getPos());
            }
            nextChar();
            return expression;
        } else if (source.test('x') || source.test('y') || source.test('z')) {
            CommonExpression variable = new Variable(Character.toString(symbol));
            nextChar();
            return variable;
        } else {
            String token = nextToken();
            if (token.equals("log2")) {
                return new CheckedLog2(parsePrimary());
            } else if (token.equals("pow2")) {
                return new CheckedPow2(parsePrimary());
            }
            throw new UnexpectedUnaryOperation(source.getPos(), source.substringWithOffset(token.length() - 1));
        }
    }
}
