package expression.generic;
//

import java.util.Objects;

public abstract class Action<T> implements UniteExpression<T> {
    protected final AbstractOperaions<T> op;
    private final UniteExpression<T> left;
    private final UniteExpression<T> right;
    private final String act;

    public Action(UniteExpression<T> left, UniteExpression<T> right, String act, AbstractOperaions<T> op) {
        this.left = left;
        this.right = right;
        this.act = act;
        this.op = op;
    }

    protected abstract T getAnswer(final T a, final T b);

    public T evaluate(T x, T y, T z) {
        return getAnswer(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    public T evaluate(T x) {
        return getAnswer(left.evaluate(x), right.evaluate(x));
    }

    public String toString() {
        return "(" + left.toString() + " " + act + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action<T> action = (Action<T>) o;
        return Objects.equals(act, action.act) && Objects.equals(right, action.right)
                && Objects.equals(left, action.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, act);
    }
}
