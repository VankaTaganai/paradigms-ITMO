package expression;

import expression.exceptions.OverflowException;
import expression.exceptions.WrongArgumentsException;

public class CheckedPow extends AbstractBinaryOperator implements CommonExpression {
    public CheckedPow(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    private int binPow(int x, int k) throws OverflowException {
        if (k == 0) {
            return 1;
        }
        if (k % 2 == 1) {
            int res = binPow(x, k - 1);
            return CheckedMultiply.multiply(res, x, "power");
        } else {
            int res = binPow(x, k / 2);
            return CheckedMultiply.multiply(res, res, "power");
        }
    }

    public int makeOperation(int leftOperand, int rightOperand) throws OverflowException {
        check(leftOperand, rightOperand);
        return binPow(leftOperand, rightOperand);
    }

    protected void check(int leftOperand, int rightOperand) throws WrongArgumentsException {
        if ((leftOperand == 0 && rightOperand == 0) || rightOperand < 0) {
            throw new WrongArgumentsException("power");
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 4;
    }

    public String getOperator() {
        return " ** ";
    }
}

