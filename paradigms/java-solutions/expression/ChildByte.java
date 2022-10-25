package expression;

public class ChildByte implements UniteExpression {
    private final UniteExpression experess;

    public ChildByte(UniteExpression experess) {
        this.experess = experess;
    }

    public int evaluate(int x, int y, int z) {
        return Integer.numberOfTrailingZeros(experess.evaluate(x, y, z));
    }

    public int evaluate(int x) {
        return Integer.numberOfTrailingZeros(experess.evaluate(x));
    }

    public String toString() {
        return "t0(" + experess.toString() + ")";
    }
}
