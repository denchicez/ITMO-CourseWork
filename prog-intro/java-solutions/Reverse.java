import java.io.IOException;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) throws IOException {
        Scanner scList = new Scanner(System.in);
        ArrayList<String> Lines = new ArrayList<>();
        while (scList.hasNextLine()) {
            Lines.add(scList.nextLine());
        }
        for (int i = Lines.size() - 1; i >= 0; i--) {
            ArrayList<Integer> Numbers = new ArrayList<>();
            Scanner ScStr = new Scanner(Lines.get(i));
            while (ScStr.hasNextAnyone()) {
                String number = ScStr.nextInt();
                int now = Integer.parseInt(number);
                Numbers.add(now);
            }
            for (int j = Numbers.size() - 1; j >= 0; j--) {
                System.out.print(Numbers.get(j) + " ");
            }
            System.out.println();
        }
    }
}