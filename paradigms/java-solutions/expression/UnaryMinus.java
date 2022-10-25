package expression;

public class UnaryMinus implements UniteExpression {
    private final UniteExpression experess;

    public UnaryMinus(UniteExpression experess) {
        this.experess = experess;
    }

    public int evaluate(int x, int y, int z) {
        return -1 * experess.evaluate(x, y, z);
    }

    public int evaluate(int x) {
        return -1 * experess.evaluate(x);
    }

    public String toString() {
        return "-(" + experess.toString() + ")";
    }
}
