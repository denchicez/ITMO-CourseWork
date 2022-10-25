package expression.generic;

import java.util.Objects;

public class Const<T> implements UniteExpression<T> {
    final private T value;

    public Const(T value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public T evaluate(T x, T y, T z) {
        return this.value;
    }

    public T evaluate(T x) {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<T> variable = (Const<T>) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
