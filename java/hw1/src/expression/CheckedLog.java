package expression;

import expression.exceptions.OverflowException;
import expression.exceptions.WrongArgumentsException;

public class CheckedLog extends AbstractBinaryOperator implements CommonExpression {
    public CheckedLog(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int makeOperation(int leftOperand, int rightOperand) throws OverflowException {
        check(leftOperand, rightOperand);
        int result = 0;
        while (leftOperand >= rightOperand) {
            leftOperand /= rightOperand;
            result++;
        }
        return result;
    }

    protected void check(int leftOperand, int rightOperand) throws WrongArgumentsException {
        if (rightOperand <= 1 || leftOperand <= 0) {
            throw new WrongArgumentsException("logarithm");
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 4;
    }

    public String getOperator() {
        return " // ";
    }
}

