import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WordStatCount {

    public static void main(String[] args) {
        try {
            String fileIn = args[0];
            String fileOut = args[1];
            ArrayList<String> allWord = readFile(fileIn);
            outFile(fileOut, allWord);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Haven't two arguments");
            System.out.println(e.getMessage());
        }

    }

    private static ArrayList<String> readFile(final String fileIn) {
        ArrayList<String> allWord = new ArrayList<>();
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
                            allWord.add(newWord);
                            l = i + 1;
                        }
                    }
                }
            }
            catch (IOException e){
                System.out.println("Problem with input");
                System.out.println(e);
            } finally {
                ScStr.reader.close();
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println(e.getMessage());
        }
        return allWord;
    }

    private static void outFile(final String fileOut, ArrayList<String> allWord) {
        ArrayList<String> uniqueWord = new ArrayList<>();
        ArrayList<Integer> counterWord = new ArrayList<>();
        for (int i = 0; i < allWord.size(); i++) {
            String wordI = allWord.get(i);
            if (wordI.length() == 0) {
                continue;
            }
            int counter = 1;
            for (int j = i + 1; j < allWord.size(); j++) {
                String wordII = allWord.get(j);
                if (wordI.compareTo(wordII) == 0) {
                    allWord.set(j, "");
                    counter += 1;
                }
            }
            uniqueWord.add(wordI);
            counterWord.add(counter);
        }
        try{
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileOut),
                            StandardCharsets.UTF_8
                    )
            );
            for (int i = uniqueWord.size() - 1; i > -1; i--) {
                for (int j = 0; j < i; j++) {
                    Integer countFirst = counterWord.get(j);
                    Integer countSecond = counterWord.get(j + 1);
                    if (countSecond < countFirst) {
                        String wordSecond = uniqueWord.get(j + 1);
                        String wordFirst = uniqueWord.get(j);
                        uniqueWord.set(j + 1, wordFirst);
                        uniqueWord.set(j, wordSecond);
                        counterWord.set(j + 1, countFirst);
                        counterWord.set(j, countSecond);
                    }
                }
            }
            try {
                for (int i = 0; i < uniqueWord.size(); i++) {
                    out.write(uniqueWord.get(i) + " " + counterWord.get(i));
                    out.newLine();
                }
            } catch(IOException e) {
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