package expression;

public class Max extends Action {
    public Max(UniteExpression left, UniteExpression right) {
        super(left, right, "max");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return Math.max(valueLeft, valueRight);
    }
}
