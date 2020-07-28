package expression;

import expression.exceptions.WrongArgumentsException;

public class CheckedLog2 extends AbstractUnaryOperator implements CommonExpression {
    public CheckedLog2(CommonExpression expression) {
        super(expression);
    }

    public int makeOperation(int operand) throws WrongArgumentsException {
        check(operand);
        int result = 0;
        while (operand >= 2) {
            operand >>= 1;
            result++;
        }
        return result;
    }

    protected void check(int value) throws WrongArgumentsException {
        if (value <= 0) {
            throw new WrongArgumentsException("logarithm");
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 5;
    }

    public String getOperator() {
        return "log2";
    }
}
