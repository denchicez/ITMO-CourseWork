import java.util.Scanner;

public class I {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long xl = Long.MAX_VALUE;
        long yl = Long.MAX_VALUE;
        long xr = Long.MIN_VALUE;
        long yr = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int h = sc.nextInt();
            xl = Math.min(xl, x - h);
            xr = Math.max(xr, x + h);
            yl = Math.min(yl, y - h);
            yr = Math.max(yr, y + h);
        }
        long forH = ((Math.max(xr - xl, yr - yl) + 1) / 2);
        System.out.print((xl + xr) / 2 + " " + (yl + yr) / 2 + " " + forH);
    }
}