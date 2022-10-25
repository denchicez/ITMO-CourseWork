import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Wspp {
    public static void main(String[] args) {
        try {
            String fileIn = args[0];
            String fileOut = args[1];
            LinkedHashMap<String, ArrayList<Integer>> wordStat = readFile(fileIn);
            outFile(fileOut, wordStat);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Haven't two arguments");
            System.out.println(e.getMessage());
        }

    }

    private static LinkedHashMap<String, ArrayList<Integer>> readFile(final String fileIn) {
        LinkedHashMap<String, ArrayList<Integer>> wordStat = new LinkedHashMap<>();
        ArrayList<String> allWord = new ArrayList<>();
        int counterWord = 0;
        try {
            Scanner ScStr = new Scanner(new FileInputStream(fileIn));
            try {
                while (ScStr.hasNextLine()) {
                    String line = ScStr.nextLine();
                    line = line.toLowerCase() + " ";
                    int l = 0;
                    for (int i = 0; i < line.length(); i++) {
                        char tempory = line.charAt(i);
                        if (!(Character.getType(tempory) == Character.LOWERCASE_LETTER ||
                                Character.getType(tempory) == Character.DASH_PUNCTUATION ||
                                tempory == '\'')) {
                            String newWord = line.substring(l, i);
                            l = i + 1;
                            if (newWord == "") {
                                continue;
                            }
                            counterWord++;
                            if (wordStat.containsKey(newWord)) {
                                ArrayList<Integer> listIndexWord = wordStat.get(newWord);
                                // System.err.println(newWord + " " + allWord.size());
                                listIndexWord.add(counterWord);
                                wordStat.put(newWord, listIndexWord);
                            } else {
                                ArrayList<Integer> listIndexWord = new ArrayList<>();
                                // System.err.println(newWord + " " + allWord.size());
                                listIndexWord.add(counterWord);
                                wordStat.put(newWord, listIndexWord);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Problem with input");
                System.out.println(e);
            } finally {
                ScStr.reader.close();
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        }
        return wordStat;
    }

    private static void outFile(final String fileOut, LinkedHashMap<String, ArrayList<Integer>> wordStat) {
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
                    ArrayList<Integer> listIndexWord = wordStat.get(nowWord);
                    for (int j = 0; j < listIndexWord.size(); j++) {
                        if (j != listIndexWord.size() - 1) {
                            out.write(listIndexWord.get(j) + " ");
                        } else {
                            out.write(listIndexWord.get(j) + "");
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