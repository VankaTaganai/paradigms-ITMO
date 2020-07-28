package expression;

import expression.evaluate.Operation;

public class Max<T> extends AbstractBinaryOperator<T> implements CommonExpression<T> {
    public Max(CommonExpression<T> left, CommonExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    public T makeOperation(T leftOperand, T rightOperand) {
        return operation.max(leftOperand, rightOperand);
    }
}