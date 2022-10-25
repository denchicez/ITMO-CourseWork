package expression.generic;

public class Max<T> extends Action<T> {
    public Max(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "max", op);
    }

    @Override
    public T getAnswer(T valueLeft, T valueRight) {
        return op.max(valueLeft, valueRight);
    }
}
