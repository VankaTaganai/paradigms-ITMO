package expression;

import expression.evaluate.Operation;
import expression.exceptions.OverflowException;

public class CheckedMultiply<T> extends AbstractBinaryOperator<T> implements CommonExpression<T> {
    public CheckedMultiply(CommonExpression<T> left, CommonExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }


    public T makeOperation(T leftOperand, T rightOperand) throws OverflowException {
        return operation.multiply(leftOperand, rightOperand);
    }
}
