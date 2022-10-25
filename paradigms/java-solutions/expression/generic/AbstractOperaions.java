package expression.generic;

public interface AbstractOperaions<T> {
    T parseNumber(String s);

    T add(T first, T second);

    T subtract(T first, T second);

    T divide(T first, T second);

    T multiply(T first, T second);

    T min(T first, T second);

    T max(T first, T second);

    T unaryMinus(T first);

    T count(T first);
}
