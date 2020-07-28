package expression;

import expression.exceptions.CalculatingException;

import java.util.Objects;

public abstract class AbstractUnaryOperator implements CommonExpression {
    private final CommonExpression expression;

    protected AbstractUnaryOperator(CommonExpression expression) {
        this.expression = expression;
    }

    public abstract int makeOperation(int operand) throws CalculatingException;

    public String toString() {
        return "(" + getOperator() + expression + ")";
    }

    public String toMiniString() {
        return toString();
    }

    public int evaluate(int x) throws CalculatingException {
        return makeOperation(expression.evaluate(x));
    }

    public int evaluate(int x, int y, int z) throws CalculatingException {
        return makeOperation(expression.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnaryOperator that = (AbstractUnaryOperator) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
