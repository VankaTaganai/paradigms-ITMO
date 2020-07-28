package expression;

import expression.evaluate.Operation;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class CheckedDivide<T> extends AbstractBinaryOperator<T> {
    public CheckedDivide(CommonExpression<T> left, CommonExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    public T makeOperation(T leftOperand, T rightOperand) throws DivisionByZeroException, OverflowException {
        return operation.divide(leftOperand, rightOperand);
    }
}