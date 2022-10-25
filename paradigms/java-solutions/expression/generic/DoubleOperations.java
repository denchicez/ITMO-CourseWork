package expression.generic;

public class DoubleOperations implements AbstractOperaions<Double> {

    public Double parseNumber(String s) {
        return Double.parseDouble(s);
    }

    public Double add(Double first, Double second) {
        return first + second;
    }

    public Double subtract(Double first, Double second) {
        return first - second;
    }

    public Double divide(Double first, Double second) {
        return first / second;
    }

    public Double multiply(Double first, Double second) {
        return first * second;
    }

    public Double min(Double first, Double second) {
        return Double.min(first, second);
    }

    public Double max(Double first, Double second) {
        return Double.max(first, second);
    }

    public Double unaryMinus(Double first) {
        return -first;
    }

    public Double count(Double first) {
        return (double) Long.bitCount(Double.doubleToLongBits(first));
    }
}
