package expression;

import expression.evaluate.Operation;

public class Count<T> extends AbstractUnaryOperator<T> {
    public Count(CommonExpression<T> expression, Operation<T> operation) {
        super(expression, operation);
    }

    public T makeOperation(T expression) {
        return operation.count(expression);
    }
}
