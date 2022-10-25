package expression.generic;

public class UnaryMinus<T> implements UniteExpression<T> {
    private final UniteExpression<T> experess;
    private final AbstractOperaions<T> op;

    public UnaryMinus(UniteExpression<T> experess, AbstractOperaions<T> op) {
        this.experess = experess;
        this.op = op;
    }

    public T evaluate(T x, T y, T z) {
        return op.unaryMinus(experess.evaluate(x, y, z));
    }

    public T evaluate(T x) {
        return op.unaryMinus(experess.evaluate(x));
    }

    public String toString() {
        return "-(" + experess.toString() + ")";
    }
}
