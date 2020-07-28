package expression;

import expression.exceptions.OverflowException;

public class CheckedAdd extends AbstractBinaryOperator implements CommonExpression {
    public CheckedAdd(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int makeOperation(int leftOperand, int rightOperand) throws OverflowException {
        check(leftOperand, rightOperand);
        return leftOperand + rightOperand;
    }

    protected void check(int leftOperand, int rightOperand) throws OverflowException {
        if (leftOperand > 0 && rightOperand > 0 && rightOperand > Integer.MAX_VALUE - leftOperand) {
            throw new OverflowException("addition");
        }
        if (leftOperand < 0 && rightOperand < 0 && rightOperand < Integer.MIN_VALUE - leftOperand) {
            throw new OverflowException("addition");
        }
    }

    public int getPriority() {
        return 1;
    }

    public boolean isPriority() {
        return false;
    }

    public String getOperator() {
        return " + ";
    }
}
