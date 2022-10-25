package expression.generic;

import expression.exceptions.OverflowException;

public class IntegerOperations implements AbstractOperaions<Integer> {

    public Integer parseNumber(String s) {
        return Integer.parseInt(s);
    }

    public Integer add(Integer first, Integer second) {
        int res = first + second;
        if (first > 0 && second > 0 && res <= 0) throw new OverflowException();
        if (first < 0 && second < 0 && res >= 0) throw new OverflowException();
        return first + second;
    }

    public Integer subtract(Integer first, Integer second) {
        int res = first - second;
        if (first > 0 && second < 0 && res <= 0) throw new OverflowException();
        if (first < 0 && second > 0 && res >= 0) throw new OverflowException();
        if (first == 0 && second == Integer.MIN_VALUE) throw new OverflowException();
        return first - second;
    }

    public Integer divide(Integer first, Integer second) {
        int res = first / second;
        if (first < 0 && second < 0 && res < 0) throw new OverflowException();
        if (first > 0 && second > 0 && res < 0) throw new OverflowException();
        if (first > 0 && second < 0 && res > 0) throw new OverflowException();
        if (first < 0 && second > 0 && res > 0) throw new OverflowException();
        return first / second;
    }

    public Integer multiply(Integer first, Integer second) {
        int res = first * second;
        if (res != 0) {
            if (res / first != second || res / second != first) throw new OverflowException();
        }
        if (first < 0 && second < 0 && res <= 0) throw new OverflowException();
        if (first > 0 && second > 0 && res <= 0) throw new OverflowException();
        if (first > 0 && second < 0 && res >= 0) throw new OverflowException();
        if (first < 0 && second > 0 && res >= 0) throw new OverflowException();
        return first * second;
    }

    public Integer min(Integer first, Integer second) {
        return Integer.min(first, second);
    }

    public Integer max(Integer first, Integer second) {
        return Integer.max(first, second);
    }

    public Integer unaryMinus(Integer first) {
        if (first == Integer.MIN_VALUE) throw new OverflowException();
        return -first;
    }

    public Integer count(Integer first) {
        return Integer.bitCount(first);
    }
}
