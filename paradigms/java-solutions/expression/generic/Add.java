package expression.generic;

public class Add<T> extends Action<T> {
    public Add(UniteExpression<T> left, UniteExpression<T> right, AbstractOperaions<T> op) {
        super(left, right, "+", op);
    }

    @Override
    public T getAnswer(T valueLeft, T valueRight) {
        return op.add(valueLeft, valueRight);
    }
}