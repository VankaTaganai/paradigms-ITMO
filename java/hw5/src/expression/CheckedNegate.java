package expression;

import expression.evaluate.Operation;
import expression.exceptions.OverflowException;

public class CheckedNegate<T> extends AbstractUnaryOperator<T> {
    public CheckedNegate(CommonExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    public T makeOperation(T expression) throws OverflowException {
        return operation.negate(expression);
    }
}
