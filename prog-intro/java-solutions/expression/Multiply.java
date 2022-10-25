package expression;

public class Multiply extends Action {
    public Multiply(UniteExpression left, UniteExpression right) {
        super(left, right, "*");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return valueLeft * valueRight;
    }
}
