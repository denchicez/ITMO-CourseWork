package expression.generic;

public class Count<T> implements UniteExpression<T> {
    private final UniteExpression<T> experess;
    protected AbstractOperaions<T> op;

    public Count(UniteExpression<T> experess, AbstractOperaions<T> op) {
        this.experess = experess;
        this.op = op;
    }

    public T evaluate(T x, T y, T z) {
        return op.count(experess.evaluate(x, y, z));
    }

    public T evaluate(T x) {
        return op.count(experess.evaluate(x));
    }

    public String toString() {
        return "count (" + experess.toString() + ")";
    }
}
