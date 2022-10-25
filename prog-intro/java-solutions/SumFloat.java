public class SumFloat {
    public static void main(final String[] args) {
        float sum = 0;
        for (String arg: args) {
            int left = 0;
            int n = arg.length(); //длина аргумента
            for (int i = 0; i < n; i++) {
                char symbol = arg.charAt(i);
                if (Character.isWhitespace(symbol)) {
                    if (left != i) {
                        String number = arg.substring(left, i);
                        sum += Float.parseFloat(number);
                    }
                    left = i + 1;
                    continue;
                }
            }
            if (left == n) {
                continue;
            }
            String number = arg.substring(left, n);
            sum += Float.parseFloat(number);

        }
        System.out.print(sum);
    }
}
