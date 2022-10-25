package expression.generic;

public class Multiply<T> extends Action<T> {
    public Multiply(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "*", op);
    }

    @Override
    public T getAnswer(T valueLeft, T valueRight) {
        return op.multiply(valueLeft, valueRight);
    }
}
