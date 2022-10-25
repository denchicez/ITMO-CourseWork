package expression;

public class Subtract extends Action {
    public Subtract(UniteExpression left, UniteExpression right) {
        super(left, right, "-");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return valueLeft - valueRight;
    }
}
