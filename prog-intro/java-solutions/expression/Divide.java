package expression;

public class Divide extends Action {
    public Divide(UniteExpression left, UniteExpression right) {
        super(left, right, "/");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return valueLeft / valueRight;
    }
}