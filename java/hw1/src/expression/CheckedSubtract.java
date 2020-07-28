package expression;

import expression.exceptions.OverflowException;

public class CheckedSubtract extends AbstractBinaryOperator implements CommonExpression {
    public CheckedSubtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int makeOperation(int leftOperand, int rightOperand) throws OverflowException {
        check(leftOperand, rightOperand);
        return leftOperand - rightOperand;
    }

    protected void check(int leftOperand, int rightOperand) throws OverflowException {
        if (leftOperand >= 0 && rightOperand < 0 && rightOperand < -Integer.MAX_VALUE + leftOperand) {
            throw new OverflowException("subtraction");
        }
        if (leftOperand < 0 && rightOperand > 0 && leftOperand < Integer.MIN_VALUE + rightOperand) {
            throw new OverflowException("subtraction");
        }
    }

    public boolean isPriority() {
        return true;
    }

    public int getPriority() {
        return 1;
    }

    public String getOperator() {
        return " - ";
    }
}

