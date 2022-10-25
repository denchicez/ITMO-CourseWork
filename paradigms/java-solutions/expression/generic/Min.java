package expression.generic;

public class Min<T> extends Action<T> {
    public Min(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "min", op);
    }

    public T getAnswer(T valueLeft, T valueRight) {
        return op.min(valueLeft, valueRight);
    }
}
