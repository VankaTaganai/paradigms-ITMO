package expression.evaluate;

public class FloatOperation implements Operation<Float> {
    public Float add(Float leftOperand, Float rightOperand) {
        return leftOperand + rightOperand;
    }

    public Float sub(Float leftOperand, Float rightOperand) {
        return leftOperand - rightOperand;
    }

    public Float multiply(Float leftOperand, Float rightOperand) {
        return leftOperand * rightOperand;
    }

    public Float divide(Float leftOperand, Float rightOperand) {
        return leftOperand / rightOperand;
    }

    public Float negate(Float operand) {
        return -operand;
    }

    public Float parse(String operand) {
        return (float) Integer.parseInt(operand);
    }

    public Float min(Float leftOperand, Float rightOperand) {
        return Float.min(leftOperand, rightOperand);
    }

    public Float max(Float leftOperand, Float rightOperand) {
        return Float.max(leftOperand, rightOperand);
    }

    public Float count(Float value) {
        return (float) Integer.bitCount(Float.floatToIntBits(value));
    }
}
