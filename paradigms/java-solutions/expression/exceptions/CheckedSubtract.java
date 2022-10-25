package expression.exceptions;

import expression.Action;
import expression.UniteExpression;

public class CheckedSubtract extends Action {
    public CheckedSubtract(UniteExpression left, UniteExpression right) {
        super(left, right, "-");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        int res = valueLeft - valueRight;
        if (valueLeft > 0 && valueRight < 0 && res <= 0) throw new OverflowException();
        if (valueLeft < 0 && valueRight > 0 && res >= 0) throw new OverflowException();
        if (valueLeft == 0 && valueRight == Integer.MIN_VALUE) throw new OverflowException();
        return res;
    }
}