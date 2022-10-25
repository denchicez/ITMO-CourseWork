package expression.generic;

import java.math.BigInteger;

public class BigIntegerOperations implements AbstractOperaions<BigInteger> {

    public BigInteger parseNumber(String s) {
        return new BigInteger(s);
    }

    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    public BigInteger subtract(BigInteger first, BigInteger second) {
        return first.subtract(second);
    }

    public BigInteger divide(BigInteger first, BigInteger second) {
        return first.divide(second);
    }

    public BigInteger multiply(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    public BigInteger min(BigInteger first, BigInteger second) {
        return first.min(second);
    }

    public BigInteger max(BigInteger first, BigInteger second) {
        return first.max(second);
    }

    public BigInteger unaryMinus(BigInteger first) {
        return first.multiply(new BigInteger("-1"));
    }

    public BigInteger count(BigInteger first) {
        return new BigInteger(String.valueOf(first.bitCount()));
    }
}
