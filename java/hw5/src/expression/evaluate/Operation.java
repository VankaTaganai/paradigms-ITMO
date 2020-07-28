package expression.evaluate;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public interface Operation<T> {
    T add(T leftOperand, T rightOperand) throws OverflowException;

    T sub(T leftOperand, T rightOperand) throws OverflowException;

    T multiply(T leftOperand, T rightOperand) throws OverflowException;

    T divide(T leftOperand, T rightOperand) throws DivisionByZeroException, OverflowException;

    T negate(T operand) throws OverflowException;

    T parse(String operand) throws OverflowException;

    T min(T leftOperand, T rightOperand);

    T max(T leftOperand, T rightOperand);

    T count(T value);
}
