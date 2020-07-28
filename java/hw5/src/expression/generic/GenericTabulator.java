package expression.generic;

import expression.CommonExpression;
import expression.evaluate.*;
import expression.exceptions.CalculatingException;
import expression.exceptions.IllegalModeException;
import expression.exceptions.ParsingException;
import expression.parser.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Operation<?>> MAP = Map.of(
            "i", new IntegerOperation(),
            "d", new DoubleOperation(),
            "bi", new BigIntegerOperation(),
            "u", new NoOverflowIntegerOperation(),
            "f", new FloatOperation(),
            "b", new ByteOperation()
    );

    private Operation<?> getOperation(String mode) throws IllegalModeException {
        Operation<?> result = MAP.get(mode);
        if (result == null) {
            throw new IllegalModeException();
        }
        return result;
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return getResult(expression, x1, x2, y1, y2, z1, z2, getOperation(mode));
    }

    private <T> Object[][][] getResult(String expression, int x1, int x2, int y1, int y2, int z1, int z2,
                                       Operation<T> operation) throws ParsingException {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        ExpressionParser<T> parser = new ExpressionParser<>(operation);
        CommonExpression<T> exp = parser.parse(expression);

        for (int i = x1; i <= x2; i++) {
            T parsedI = operation.parse(Integer.toString(i));
            for (int j = y1; j <= y2; j++) {
                T parsedJ = operation.parse(Integer.toString(j));
                for (int k = z1; k <= z2; k++) {
                    T parsedK = operation.parse(Integer.toString(k));
                    try {
                        result[i - x1][j - y1][k - z1] = exp.evaluate(parsedI, parsedJ, parsedK);
                    } catch (CalculatingException ignored) {}
                }
            }
        }
        return result;
    }
}
