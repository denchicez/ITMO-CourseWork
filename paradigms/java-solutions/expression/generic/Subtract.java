package expression.generic;

public class Subtract<T> extends Action<T> {
    public Subtract(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "-", op);
    }

    @Override
    public T getAnswer(T valueLeft, T valueRight) {
        return op.subtract(valueLeft, valueRight);
    }
}
