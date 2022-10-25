package expression;

public class Add extends Action {
    public Add(UniteExpression left, UniteExpression right) {
        super(left, right, "+");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        return valueLeft + valueRight;
    }
}