package expression.generic;

import expression.ToMiniString;

public interface Expression<T> extends ToMiniString {
    T evaluate(T x);
}
