package expression;

import expression.exceptions.OverflowException;

public class CheckedNegate extends AbstractUnaryOperator implements CommonExpression {
    public CheckedNegate(CommonExpression expression) {
        super(expression);
    }

    public int makeOperation(int expression) throws OverflowException {
        check(expression);
        return -expression;
    }

    protected void check(int value) throws OverflowException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("negation");
        }
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 5;
    }

    public String getOperator() {
        return "-";
    }
}
