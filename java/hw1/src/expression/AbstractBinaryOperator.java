package expression;

import expression.exceptions.CalculatingException;

import java.util.Objects;

public abstract class AbstractBinaryOperator implements CommonExpression {
    private final CommonExpression left;
    private final CommonExpression right;

    protected AbstractBinaryOperator(CommonExpression left, CommonExpression right) {
        this.left = left;
        this.right = right;
    }

    public abstract int makeOperation(int leftOperand, int rightOperand) throws CalculatingException;

    private String getExpression(boolean brackets, Expression expression) {
        return brackets ? "(" + expression.toMiniString() + ")" : expression.toMiniString();
    }

    public String toString() {
        return "(" + left + getOperator() + right + ")";
    }

    public String toMiniString() {
        return getExpression(left.getPriority() < getPriority(), left)
                + getOperator() + getExpression(right.getPriority() < getPriority()
                || (right.getPriority() == getPriority() && (this.isPriority()|| right.isPriority())), right);
    }

    public int evaluate(int x) throws CalculatingException {
        return makeOperation(left.evaluate(x), right.evaluate(x));
    }

    public int evaluate(int x, int y, int z) throws CalculatingException {
        return makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperator that = (AbstractBinaryOperator) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}