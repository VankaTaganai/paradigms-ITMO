package expression.evaluate;


import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerOperation implements Operation<BigInteger> {
    public BigInteger add(BigInteger leftOperand, BigInteger rightOperand){
        return leftOperand.add(rightOperand);
    }

    public BigInteger sub(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.subtract(rightOperand);
    }

    public BigInteger multiply(BigInteger leftOperand, BigInteger rightOperand)  {
        return leftOperand.multiply(rightOperand);
    }

    public BigInteger divide(BigInteger leftOperand, BigInteger rightOperand) throws DivisionByZeroException {
        checkDiv(rightOperand);
        return leftOperand.divide(rightOperand);
    }

    private void checkDiv(BigInteger rightOperand) throws DivisionByZeroException {
        if (rightOperand.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException();
        }
    }

    public BigInteger negate(BigInteger operand) {
        return operand.negate();
    }

    public BigInteger parse(String operand) {
        return new BigInteger(operand);
    }

    public BigInteger min(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.compareTo(rightOperand) <= 0 ? leftOperand : rightOperand;
    }

    public BigInteger max(BigInteger leftOperand, BigInteger rightOperand) {
        return leftOperand.compareTo(rightOperand) >= 0 ? leftOperand : rightOperand;
    }

    public BigInteger count(BigInteger value) {
        return BigInteger.valueOf(value.bitCount());
    }
}
