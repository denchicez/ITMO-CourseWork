package expression.generic;

import expression.ToMiniString;

public interface TripleExpression<T> extends ToMiniString {
    T evaluate(T x, T y, T z);
}
