import java.io.*;
import java.util.*;

public class Summ {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double xl = Double.MAX_VALUE;
        double yl = Double.MAX_VALUE;
        double xr = Double.MIN_VALUE;
        double yr = Double.MIN_VALUE;
        double forH;
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int h = sc.nextInt();
            xl = Math.min(xl, x - h);
            xr = Math.max(xr, x + h);
            yl = Math.min(yl, y - h);
            yr = Math.max(yr, y + h);
        }
        forH = Math.ceil((Math.max(xr - xl, yr - yl)) / 2);
        System.out.print((xl + xr) / 2);
        System.out.print(" ");
        System.out.print((yl + yr) / 2);
        System.out.print(" ");
        System.out.print(forH);
    }
}