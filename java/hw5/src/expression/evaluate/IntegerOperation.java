package expression.evaluate;

import expression.exceptions.CalculatingException;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerOperation implements Operation<Integer> {

    public Integer add(Integer leftOperand, Integer rightOperand) throws OverflowException {
        checkAdd(leftOperand, rightOperand);
        return leftOperand + rightOperand;
    }

    private void checkAdd(Integer leftOperand, Integer rightOperand) throws OverflowException {
        if (leftOperand > 0 && rightOperand > 0 && rightOperand > Integer.MAX_VALUE - leftOperand) {
            throw new OverflowException("addition");
        }
        if (leftOperand < 0 && rightOperand < 0 && rightOperand < Integer.MIN_VALUE - leftOperand) {
            throw new OverflowException("addition");
        }
    }

    public Integer sub(Integer leftOperand, Integer rightOperand) throws OverflowException {
        checkSub(leftOperand, rightOperand);
        return leftOperand - rightOperand;
    }

    private void checkSub(Integer leftOperand, Integer rightOperand) throws OverflowException {
        if (leftOperand >= 0 && rightOperand < 0 && rightOperand < -Integer.MAX_VALUE + leftOperand) {
            throw new OverflowException("subtraction");
        }
        if (leftOperand < 0 && rightOperand > 0 && leftOperand < Integer.MIN_VALUE + rightOperand) {
            throw new OverflowException("subtraction");
        }
    }

    public Integer multiply(Integer leftOperand, Integer rightOperand) throws OverflowException {
        checkMul(leftOperand, rightOperand, "multiply");
        return leftOperand * rightOperand;
    }

    private void checkMul(Integer leftOperand, Integer rightOperand, String type) throws OverflowException {
        if (leftOperand < 0) {
            if (rightOperand > 0 && leftOperand < Integer.MIN_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
            if (rightOperand < 0 && leftOperand < Integer.MAX_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
        }
        if (leftOperand > 0) {
            if (rightOperand > 0 && leftOperand > Integer.MAX_VALUE / rightOperand) {
                throw new OverflowException(type);
            }
            if (rightOperand < 0 && rightOperand < Integer.MIN_VALUE / leftOperand) {
                throw new OverflowException(type);
            }
        }
    }

    public Integer divide(Integer leftOperand, Integer rightOperand) throws DivisionByZeroException, OverflowException {
        checkDiv(leftOperand, rightOperand);
        return leftOperand / rightOperand;
    }

    private void checkDiv(Integer leftOperand, Integer rightOperand) throws CalculatingException {
        if (rightOperand == 0) {
            throw new DivisionByZeroException();
        }
        if (leftOperand == Integer.MIN_VALUE && rightOperand == -1) {
            throw new OverflowException("division");
        }
    }

    public Integer negate(Integer operand) throws OverflowException {
        checkNegate(operand);
        return -operand;
    }

    private void checkNegate(Integer value) throws OverflowException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("negation");
        }
    }

    public Integer parse(String operand) throws OverflowException {
        try {
            return Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            throw new OverflowException("constant");
        }
    }

    public Integer min(Integer leftOperand, Integer rightOperand) {
        return Integer.min(leftOperand, rightOperand);
    }

    public Integer max(Integer leftOperand, Integer rightOperand) {
        return Integer.max(leftOperand, rightOperand);
    }

    public Integer count(Integer value) {
        return Integer.bitCount(value);
    }
}
