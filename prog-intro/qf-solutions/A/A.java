import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int n = sc.nextInt();
        int ans = (int) Math.ceil((double) (n - b) / (b - a)) * 2 + 1;
        System.out.println(ans);
    }
}