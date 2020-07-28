package expression;

import expression.evaluate.Operation;

public class Min<T> extends AbstractBinaryOperator<T> implements CommonExpression<T> {
    public Min(CommonExpression<T> left, CommonExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    public T makeOperation(T leftOperand, T rightOperand) {
        return operation.min(leftOperand, rightOperand);
    }
}