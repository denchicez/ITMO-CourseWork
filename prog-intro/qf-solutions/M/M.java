import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class M {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            int[] a = new int[n];
            for (int j = 0; j < n; j++) {
                a[j] = sc.nextInt();
            }
            int ans = completeTest(n, a);
            System.out.println(ans);
        }
    }

    static int completeTest(int n, int[] a) {
        int ans = 0;
        Map<Integer, Integer> counters = new HashMap<Integer, Integer>();
        for (int j = n - 1; j > -1; j--) {
            for (int i = 0; i < j; i++) {
                int ak = a[j] * 2 - a[i];
                if (counters.containsKey(ak)) {
                    ans += counters.get(ak);
                }
            }
            counters.put(a[j], counters.getOrDefault(a[j], 0) + 1);

        }
        return ans;
    }
}