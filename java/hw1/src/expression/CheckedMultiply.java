package expression;

import expression.exceptions.OverflowException;

public class CheckedMultiply extends AbstractBinaryOperator implements CommonExpression {
    public CheckedMultiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    static int multiply(int leftOperand, int rightOperand, String type) {
        check(leftOperand, rightOperand, type);
        return leftOperand * rightOperand;
    }

    public int makeOperation(int leftOperand, int rightOperand) throws OverflowException {
        return multiply(leftOperand, rightOperand, "multiply");
    }

    static protected void check(int leftOperand, int rightOperand, String type) throws OverflowException {
        if (leftOperand < 0) {
            if (rightOperand > 0 && leftOperand < Integer.MIN_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
            if (rightOperand < 0 && leftOperand < Integer.MAX_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
        }
        if (leftOperand > 0) {
            if (rightOperand > 0 && leftOperand > Integer.MAX_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
            if (rightOperand < 0 && rightOperand < Integer.MIN_VALUE / leftOperand) {
                throw new OverflowException(type);
            }
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 2;
    }

    public String getOperator() {
        return " * ";
    }
}
