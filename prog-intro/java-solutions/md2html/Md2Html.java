package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Md2Html {
    private static final String separator = System.lineSeparator();

    public static void main(String[] args) throws IOException {
        String fileIn = args[0];
        String fileOut = args[1];
        Scanner in = new Scanner(new FileInputStream(fileIn));
        try {
            String htmlText = converter2Html(in);
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileOut),
                            StandardCharsets.UTF_8
                    )
            );
            try {
                out.write(htmlText);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.err.println("Apple pie is the best pie");
        } finally {
            in.reader.close();
        }
    }

    public static StringBuilder soloConverter(StringBuilder htmlText, Map<String, Tags> positionInput) {
        int inputStar = positionInput.get("*").getOpenIndex();
        int inputUnder = positionInput.get("_").getOpenIndex();
        if (inputStar != -1) {
            htmlText.insert(inputStar, "*");
        }
        if (inputUnder != -1) {
            if (inputStar == -1 || inputStar > inputUnder) {
                htmlText.insert(inputUnder, "_");
            } else {
                htmlText.insert(inputUnder + 1, "_");
            }
        }
        return htmlText;
    }

    public static void specialSymbolsConverter(StringBuilder htmlText, String out) {
        Map<String, String> specialSymbols = new HashMap<>();
        specialSymbols.put("<", "&lt;");
        specialSymbols.put(">", "&gt;");
        specialSymbols.put("&", "&amp;");
        htmlText.append(specialSymbols.getOrDefault(out, out));
    }

    public static void handlerBlock(StringBuilder blockMd, StringBuilder htmlText) {
        String blockHtml = levelBlock(blockToHtml(blockMd.append("  ")));
        if (blockHtml.length() != 7) { //empty line
            htmlText.append(blockHtml);
            htmlText.append(separator);
        }
    }

    public static String converter2Html(Scanner in) throws IOException {
        StringBuilder htmlText = new StringBuilder();
        StringBuilder blockMd = new StringBuilder();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (blockMd.length() != 0) {
                blockMd.append(separator);
            }
            blockMd.append(line);
            if (line.length() == 0) {
                handlerBlock(blockMd, htmlText);
                blockMd = new StringBuilder();
            }
        }
        handlerBlock(blockMd, htmlText);
        return htmlText.toString();
    }

    public static String levelBlock(StringBuilder s) {
        StringBuilder levelBlockText = new StringBuilder();
        int size = 0;
        boolean findFirstSymbols = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '#' && !findFirstSymbols) {
                findFirstSymbols = true;
                if (s.charAt(i) != ' ') {
                    for (int j = 0; j < size; j++) {
                        levelBlockText.append('#');
                    }
                    size = 0;
                } else {
                    continue;
                }
            }
            if (s.charAt(i) == '#' && !findFirstSymbols) {
                size++;
            }
            if (findFirstSymbols) {
                levelBlockText.append(s.charAt(i));
            }
        }
        levelBlockText = strip(levelBlockText);
        if (s.length() > 0 && s.charAt(0) == ' ') {
            levelBlockText.insert(0, " ");
        }
        if (size == 0) {
            levelBlockText.insert(0, "<p>");
            levelBlockText.append("</p>");
        } else {
            levelBlockText.insert(0, "<h" + size + ">");
            levelBlockText.append("</h" + size + ">");
        }
        return levelBlockText.toString();
    }

    public static StringBuilder strip(StringBuilder s) {
        int l = 0;
        int r = s.length() - 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '\n' && s.charAt(i) != '\r') {
                l = i;
                break;
            }
        }
        for (int i = s.length() - 1; i > -1; i--) {
            if (s.charAt(i) != '\n' && s.charAt(i) != '\r') {
                r = i;
                break;
            }
        }

        return new StringBuilder(s.substring(l, r + 1));
    }

    public static StringBuilder blockToHtml(StringBuilder s) {
        StringBuilder htmlText = new StringBuilder();
        Map<String, Tags> tags = new HashMap<>();
        List<Character> buffer = new ArrayList<>(3);
        tags.put("`", new Tags(-1, "<code>", "</code>"));
        tags.put("*", new Tags(-1, "<em>", "</em>"));
        tags.put("**", new Tags(-1, "<strong>", "</strong>"));
        tags.put("--", new Tags(-1, "<s>", "</s>"));
        tags.put("_", new Tags(-1, "<em>", "</em>"));
        tags.put("__", new Tags(-1, "<strong>", "</strong>"));
        tags.put("''", new Tags(-1, "<q>", "</q>"));
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            buffer.add(ch);
            if (buffer.size() == 3) {
                String firstSymbol = Character.toString(buffer.remove(0));
                String secondSymbol = Character.toString(buffer.get(0));
                if (tags.containsKey(firstSymbol + secondSymbol)) {
                    buffer.remove(0);
                    addTag(tags.get(firstSymbol + secondSymbol), htmlText);
                } else if (tags.containsKey(firstSymbol)) {
                    addTag(tags.get(firstSymbol), htmlText);
                } else if (firstSymbol.equals("\\")) {
                    specialSymbolsConverter(htmlText, Character.toString(buffer.remove(0)));
                } else {
                    specialSymbolsConverter(htmlText, firstSymbol);
                }
            }
        }
        htmlText = soloConverter(htmlText, tags);
        return htmlText;
    }

    public static void addTag(Tags tag, StringBuilder htmlText) {
        if (tag.getOpenIndex() == -1) {
            tag.replaceOpenIndex(htmlText.length());
        } else {
            htmlText.insert(tag.getOpenIndex(), tag.openTag);
            tag.replaceOpenIndex(-1);
            htmlText.append(tag.closeTag);
        }
    }

}