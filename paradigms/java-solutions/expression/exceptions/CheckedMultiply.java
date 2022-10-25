package expression.exceptions;

import expression.Action;
import expression.UniteExpression;

public class CheckedMultiply extends Action {
    public CheckedMultiply(UniteExpression left, UniteExpression right) {
        super(left, right, "*");
    }

    @Override
    public int getAnswer(int valueLeft, int valueRight) {
        int res = valueLeft * valueRight;
        if (res != 0) {
            if (res / valueLeft != valueRight || res / valueRight != valueLeft) throw new OverflowException();
        }
        if (valueLeft < 0 && valueRight < 0 && res <= 0) throw new OverflowException();
        if (valueLeft > 0 && valueRight > 0 && res <= 0) throw new OverflowException();
        if (valueLeft > 0 && valueRight < 0 && res >= 0) throw new OverflowException();
        if (valueLeft < 0 && valueRight > 0 && res >= 0) throw new OverflowException();
        return valueLeft * valueRight;
    }
}