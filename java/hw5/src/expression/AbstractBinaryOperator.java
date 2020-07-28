package expression;

import expression.evaluate.Operation;
import expression.exceptions.CalculatingException;


public abstract class AbstractBinaryOperator<T> implements CommonExpression<T> {
    private final CommonExpression<T> left;
    private final CommonExpression<T> right;
    protected final Operation<T> operation;

    protected AbstractBinaryOperator(CommonExpression<T> left, CommonExpression<T> right, Operation<T> operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public abstract T makeOperation(T leftOperand, T rightOperand) throws CalculatingException;

    public T evaluate(T x, T y, T z) throws CalculatingException {
        return makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}