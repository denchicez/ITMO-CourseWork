import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WsppSortedPosition {

    public static void main(String[] args) {
        try {
            String fileIn = args[0];
            String fileOut = args[1];
            Map<String, ArrayList<HandMadePair>> wordStat = readFile(fileIn);
            outFile(fileOut, wordStat);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Haven't two arguments");
            System.out.println(e.getMessage());
        }

    }

    private static Map<String, ArrayList<HandMadePair>> readFile(final String fileIn) {
        Map<String, ArrayList<HandMadePair>> wordStat = new TreeMap<>();
        try {
            int lineNumber = 1;
            int inLineNumber = 1;
            Scanner ScStr = new Scanner(new FileInputStream(fileIn));
            try {
                while (ScStr.hasNext()) {
                    String word = ScStr.next();
                    word = word.toLowerCase();
                    if (wordStat.containsKey(word)) {
                        ArrayList<HandMadePair> listIndexWord = wordStat.get(word);
                        listIndexWord.add(new HandMadePair(lineNumber, inLineNumber));
                        wordStat.put(word, listIndexWord);
                    } else {
                        ArrayList<HandMadePair> listIndexWord = new ArrayList<>();
                        listIndexWord.add(new HandMadePair(lineNumber, inLineNumber));
                        wordStat.put(word, listIndexWord);
                    }
                    if (ScStr.isEnd()) {
                        lineNumber++;
                        inLineNumber = 0;
                    }
                    inLineNumber++;
                }
                ScStr.reader.close();
            } catch (IOException e) {
                System.out.println("Problem with input");
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        }
        return wordStat;
    }

    private static void outFile(final String fileOut, Map<String, ArrayList<HandMadePair>> wordStat) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileOut),
                            StandardCharsets.UTF_8
                    )
            );
            try {
                Set<String> keys = wordStat.keySet();
                Object[] arr = keys.toArray();
                for (int i = 0; i < arr.length; i++) {
                    String nowWord = String.valueOf(arr[i]);
                    out.write(nowWord + " " + wordStat.get(nowWord).size() + " ");
                    ArrayList<HandMadePair> listPosition = wordStat.get(nowWord);
                    for (int j = 0; j < listPosition.size(); j++) {
                        int firstOut = listPosition.get(j).first;
                        int secondOut = listPosition.get(j).second;
                        out.write(firstOut + ":" + secondOut);
                        if (j != listPosition.size() - 1) {
                            out.write(" ");
                        }
                    }
                    out.newLine();
                }
            } catch (IOException e) {
                System.out.println("Problem with out when outing");
                System.out.println(e.getMessage());
            } finally {
                out.close();
            }
        } catch (IOException e) {
            System.out.println("Problem with out");
            System.out.println(e.getMessage());
        }
    }

}