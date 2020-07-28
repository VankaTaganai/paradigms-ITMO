package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;
import expression.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        ExpressionParser parser = new ExpressionParser();
        CommonExpression expression = null;
        try {
            expression = parser.parse("x x");
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 10; i++) {
            System.out.print(i + " ");
            try {
                System.out.println(expression.evaluate(i));
            } catch (DivisionByZeroException | OverflowException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
