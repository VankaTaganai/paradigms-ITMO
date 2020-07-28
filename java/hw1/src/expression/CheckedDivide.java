package expression;

import expression.exceptions.CalculatingException;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends AbstractBinaryOperator {
    public CheckedDivide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    public int makeOperation(int leftOperand, int rightOperand) throws DivisionByZeroException, OverflowException {
        check(leftOperand, rightOperand);
        return leftOperand / rightOperand;
    }

    protected void check(int leftOperand, int rightOperand) throws CalculatingException {
        if (rightOperand == 0) {
            throw new DivisionByZeroException();
        }
        if (leftOperand == Integer.MIN_VALUE && rightOperand == -1) {
            throw new OverflowException("division");
        }
    }

    public boolean isPriority() {
        return true;
    }

    public int getPriority() {
        return 2;
    }

    public String getOperator() {
        return " / ";
    }
}