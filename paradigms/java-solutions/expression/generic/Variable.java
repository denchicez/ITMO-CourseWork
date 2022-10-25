package expression.generic;

import java.util.Objects;

public class Variable<T> implements UniteExpression<T> {
    private final String value;

    public Variable(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public T evaluate(T x, T y, T z) {
        return switch (this.value) {
            case ("x") -> x;
            case ("y") -> y;
            default -> z;
        };
    }

    public T evaluate(T x) {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable<T> variable = (Variable) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}