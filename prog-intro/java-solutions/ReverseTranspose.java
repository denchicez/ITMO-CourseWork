import java.util.Scanner;
public class ReverseTranspose {
    public static void main(String[] args) {
        Scanner ScList = new Scanner(System.in);
        int[] massiv = new int[1_000_000];
        int n = 0; // высота
        int maxWidth = 0;
        int[][] matrix = new int[1_000_000][];
        while (ScList.hasNextLine()) {
            String line = ScList.nextLine();
            Scanner ScStr = new Scanner(line);
            int m = 0; // ширина
            while (ScStr.hasNextInt()) {
                int number = ScStr.nextInt();
                massiv[m] = number;
                m++;
            }
            maxWidth = Math.max(maxWidth, m);
            matrix[n] = new int[m];
            for (int j = 0; j < m; j++) {
                matrix[n][j] = massiv[j];
            }
            n++;
        }
        for (int j = 0; j < maxWidth; j++) {
            for (int i = 0; i < n; i++) {
                if (matrix[i].length <= j) {
                    continue;
                }
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}