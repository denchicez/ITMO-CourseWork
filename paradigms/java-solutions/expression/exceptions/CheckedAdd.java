package expression.exceptions;
import expression.Action;
import expression.UniteExpression;

public class CheckedAdd extends Action {
    public CheckedAdd(UniteExpression left, UniteExpression right) {
        super(left, right, "+");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) throws OverflowException {
        int res = valueLeft + valueRight;
        if (valueLeft > 0 && valueRight > 0 && res <= 0) throw new OverflowException();
        if (valueLeft < 0 && valueRight < 0 && res >= 0) throw new OverflowException();
        return valueLeft + valueRight;
    }
}