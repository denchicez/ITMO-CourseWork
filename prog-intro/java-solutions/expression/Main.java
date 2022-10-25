package expression;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int x;
        Scanner scanner = new Scanner(System.in);
        x = scanner.nextInt();
        int answer = new Add(new Subtract(
                new Multiply(
                        new Variable("x"),
                        new Variable("x")
                ),
                new Multiply(
                        new Const(2),
                        new Variable("x")
                )
        ), new Const(1)).evaluate(x, 1, 1);
        System.out.println(answer);
    }
}