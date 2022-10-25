package expression;

public class AdultByte implements UniteExpression {
    private final UniteExpression experess;

    public AdultByte(UniteExpression experess) {
        this.experess = experess;
    }

    public int evaluate(int x, int y, int z) {
        return Integer.numberOfLeadingZeros(experess.evaluate(x, y, z));
    }

    public int evaluate(int x) {
        return Integer.numberOfLeadingZeros(experess.evaluate(x));
    }

    public String toString() {
        return "l0(" + experess.toString() + ")";
    }
}
