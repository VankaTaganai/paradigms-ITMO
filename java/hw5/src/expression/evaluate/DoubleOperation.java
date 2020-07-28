package expression.evaluate;


public class DoubleOperation implements Operation<Double> {
    public Double add(Double leftOperand, Double rightOperand) {
        return leftOperand + rightOperand;
    }

    public Double sub(Double leftOperand, Double rightOperand) {
        return leftOperand - rightOperand;
    }

    public Double multiply(Double leftOperand, Double rightOperand) {
        return leftOperand * rightOperand;
    }

    public Double divide(Double leftOperand, Double rightOperand) {
        return leftOperand / rightOperand;
    }

    public Double negate(Double operand) {
        return -operand;
    }

    public Double parse(String operand) {
        return Double.parseDouble(operand);
    }

    public Double min(Double leftOperand, Double rightOperand) {
        return Double.min(leftOperand, rightOperand);
    }

    public Double max(Double leftOperand, Double rightOperand) {
        return Double.max(leftOperand, rightOperand);
    }

    public Double count(Double value) {
        return (double) Long.bitCount(Double.doubleToLongBits(value));
    }
}
