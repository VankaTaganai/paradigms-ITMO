package expression;

import expression.exceptions.CalculatingException;
import expression.exceptions.OverflowException;
import expression.exceptions.WrongArgumentsException;

public class CheckedPow2 extends AbstractUnaryOperator implements CommonExpression {
    public CheckedPow2(CommonExpression expression) {
        super(expression);
    }

    public int makeOperation(int operand) throws CalculatingException {
        check(operand);
        return 1 << operand;
    }

    private void checkOverflow(int operand) throws OverflowException {
        if (operand >= 31) {
            throw new OverflowException("power");
        }
    }

    protected void check(int operand) throws CalculatingException {
        checkOverflow(operand);
        if (operand < 0) {
            throw new WrongArgumentsException("power");
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 5;
    }

    public String getOperator() {
        return "pow2";
    }
}
