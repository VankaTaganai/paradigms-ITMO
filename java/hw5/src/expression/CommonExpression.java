package expression;

import expression.exceptions.CalculatingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CommonExpression<T> {
    T evaluate(T x, T y, T z) throws CalculatingException;
}