package expression;

import expression.evaluate.Operation;
import expression.exceptions.CalculatingException;


public abstract class AbstractUnaryOperator<T> implements CommonExpression<T> {
    private final CommonExpression<T> expression;
    protected final Operation<T> operation;

    protected AbstractUnaryOperator(CommonExpression<T> expression, Operation<T> operation) {
        this.expression = expression;
        this.operation = operation;
    }

    public abstract T makeOperation(T operand) throws CalculatingException;

    public T evaluate(T x, T y, T z) throws CalculatingException {
        return makeOperation(expression.evaluate(x, y, z));
    }

}
