package expression;

public interface CommonExpression extends TripleExpression, Expression {
    int getPriority();
    boolean equals(Object e);
    boolean isPriority();
    String getOperator();
}
