package expression;

public class Variable<T> implements CommonExpression<T> {
    private String var;

    public Variable(String var) {
        this.var = var;
    }

    public T evaluate(T x, T y, T z) {
        if (var.equals("x")) {
            return x;
        } else if (var.equals("y")) {
            return y;
        } else {
            return z;
        }
    }
}
