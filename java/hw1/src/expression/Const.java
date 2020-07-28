package expression;

public class Const implements CommonExpression {
    private int value;


    public Const(int value) {
        this.value = value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public String toMiniString() {
        return toString();
    }

    public int evaluate(int x) {
        return value;
    }

    public int evaluate(int x, int y, int z) {
        return value;
    }

    public boolean isPriority() {
        return false;
    }

    public int getPriority() {
        return 3;
    }

    public String getOperator() {
        return "";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Const) {
            Const constant = (Const) expression;
            return toString().equals(constant.toString());
        }
        return false;
    }
}
