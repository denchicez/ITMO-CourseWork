package expression;

import java.util.Objects;

public class Variable implements UniteExpression {
    private final String value;

    public Variable(String value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public int evaluate(int x, int y, int z) {
        switch (this.value) {
            case ("x"):
                return x;
            case ("y"):
                return y;
            default:
                return z;
        }
    }

    public int evaluate(int x) {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}