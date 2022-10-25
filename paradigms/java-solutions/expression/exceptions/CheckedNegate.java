package expression.exceptions;

import expression.UniteExpression;

public class CheckedNegate implements UniteExpression {
    private final UniteExpression experess;

    public CheckedNegate(UniteExpression experess) {
        this.experess = experess;
    }

    public int evaluate(int x, int y, int z) {
        int res = experess.evaluate(x, y, z);
        if (res == Integer.MIN_VALUE) throw new OverflowException();
        return -1 * res;
    }

    public int evaluate(int x) {
        int res = experess.evaluate(x);
        if (res == Integer.MIN_VALUE) throw new OverflowException();
        return -1 * experess.evaluate(x);
    }

    public String toString() {
        return "-(" + experess.toString() + ")";
    }
}
