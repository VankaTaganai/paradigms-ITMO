package expression.evaluate;

import expression.exceptions.DivisionByZeroException;

public class ByteOperation implements Operation<Byte> {
    public Byte add(Byte leftOperand, Byte rightOperand) {
        return (byte) (leftOperand +  rightOperand);
    }

    public Byte sub(Byte leftOperand, Byte rightOperand) {
        return (byte) (leftOperand - rightOperand);
    }

    public Byte multiply(Byte leftOperand, Byte rightOperand) {
        return (byte) (leftOperand * rightOperand);
    }

    public Byte divide(Byte leftOperand, Byte rightOperand) {
        checkDiv(rightOperand);
        return (byte) (leftOperand / rightOperand);
    }

    private void checkDiv(Byte rightOperand) throws DivisionByZeroException {
        if (rightOperand == 0) {
            throw new DivisionByZeroException();
        }
    }

    public Byte negate(Byte operand) {
        return (byte) (-operand);
    }

    public Byte parse(String operand) {
        return (byte) Integer.parseInt(operand);
    }

    public Byte min(Byte leftOperand, Byte rightOperand) {
        return (byte) Integer.min(leftOperand, rightOperand);
    }

    public Byte max(Byte leftOperand, Byte rightOperand) {
        return (byte) Integer.max(leftOperand, rightOperand);
    }

    public Byte count(Byte value) {
        return (byte) (Integer.bitCount(value & 0xff));
    }
}
