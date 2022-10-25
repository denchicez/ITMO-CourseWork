package expression.generic;

public class Divide<T> extends Action<T> {
    public Divide(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "/", op);
    }

    @Override
    public T getAnswer(T valueLeft, T valueRight) {
        return op.divide(valueLeft, valueRight);
    }
}