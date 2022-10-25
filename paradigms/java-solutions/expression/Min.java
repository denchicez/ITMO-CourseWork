package expression;

public class Min extends Action {
    public Min(UniteExpression left, UniteExpression right) {
        super(left, right, "min");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return Math.min(valueLeft, valueRight);
    }
}
