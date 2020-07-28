package expression.evaluate;

import expression.exceptions.DivisionByZeroException;

public class NoOverflowIntegerOperation implements Operation<Integer> {

    public Integer add(Integer leftOperand, Integer rightOperand) {
        return leftOperand + rightOperand;
    }

    public Integer sub(Integer leftOperand, Integer rightOperand) {
        return leftOperand - rightOperand;
    }

    public Integer multiply(Integer leftOperand, Integer rightOperand) {
        return leftOperand * rightOperand;
    }

    public Integer divide(Integer leftOperand, Integer rightOperand) throws DivisionByZeroException {
        checkDiv(rightOperand);
        return leftOperand / rightOperand;
    }

    private void checkDiv(Integer rightOperand) throws DivisionByZeroException {
        if (rightOperand == 0) {
            throw new DivisionByZeroException();
        }
    }

    public Integer negate(Integer operand) {
        return -operand;
    }

    public Integer parse(String operand) {
        return Integer.parseInt(operand);
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
