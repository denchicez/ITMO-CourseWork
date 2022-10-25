package expression.generic;

public class Main {
    public static void main(String[] args) {
        Object[][][] result = new Object[5][5][5];
        result = new GenericTabulator().tabulate(args[0], args[1], -2, 2, -2, 2, -2, 2);
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.print("i is ");
                    System.out.print(i);
                    System.out.print(", j is ");
                    System.out.print(j);
                    System.out.print(", k is ");
                    System.out.print(k);
                    System.out.print(", Answer is ");
                    System.out.println(result[i][j][k]);
                }
            }
        }
    }
}