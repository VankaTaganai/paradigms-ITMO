package expression;

public class Variable implements CommonExpression {
    private String var;

    public Variable(String var) {
        this.var = var;
    }

    public String toString() {
        return var;
    }

    public String toMiniString() {
        return toString();
    }

    public int evaluate(int x) {
        return x;
    }

    public boolean isPriority() {
        return false;
    }

    public int evaluate(int x, int y, int z) {
        if (var.equals("x")) {
            return x;
        } else if (var.equals("y")) {
            return y;
        } else {
            return z;
        }
    }

    public int getPriority() {
        return 5;
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
        if (expression instanceof Variable) {
            Variable variable = (Variable) expression;
            return toString().equals(variable.toString());
        }
        return false;
    }
}
