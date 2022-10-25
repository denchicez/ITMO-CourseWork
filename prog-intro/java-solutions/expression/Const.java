package expression;

import java.util.Objects;

public class Const implements UniteExpression {
    final private int value;

    public Const(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public int evaluate(int x, int y, int z) {
        return this.value;
    }

    public int evaluate(int x) {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const variable = (Const) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
