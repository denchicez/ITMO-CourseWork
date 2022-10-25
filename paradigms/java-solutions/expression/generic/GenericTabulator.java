package expression.generic;

import expression.exceptions.OverflowException;

public class GenericTabulator implements Tabulator {

    private AbstractOperaions<?> chooseType(String mode) {
        if (mode.equals("i")) return new IntegerOperations();
        else if (mode.equals("bi")) return new BigIntegerOperations();
        else return new DoubleOperations();
    }

    private <T> Object[][][] answer(final AbstractOperaions<T> op, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        final ExpressionParser<T> parser = new ExpressionParser<>(op);
        UniteExpression<T> parse;
        parse = parser.parse(expression);
        T new_i, new_j, new_k;
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        new_i = op.parseNumber(Integer.toString(i));
                        new_j = op.parseNumber(Integer.toString(j));
                        new_k = op.parseNumber(Integer.toString(k));
                        result[i - x1][j - y1][k - z1] = parse.evaluate(new_i, new_j, new_k);
                    } catch (OverflowException | ArithmeticException e) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return answer(chooseType(mode), expression, x1, x2, y1, y2, z1, z2);
    }

}

//  ERROR: AssertionError: table[0][0][0](x=-2, y=-5, z=-7]) = null (expected 1)
//        mode=bi, x=[-2, 0] y=[-5, 1] z=[-7, 5], expression=-(-1)
//Exception in thread "main" java.lang.AssertionError: table[0][0][0](x=-2, y=-5, z=-7]) = null (expected 1)
//mode=bi, x=[-2, 0] y=[-5, 1] z=[-7, 5], expression=-(-1)