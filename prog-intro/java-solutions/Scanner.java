import java.io.*;
import java.nio.charset.StandardCharsets;

class Scanner {
    public BufferedReader reader;
    private String separator = System.lineSeparator();

    public Scanner(InputStream source) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Scanner(String source) {
        this.reader = new BufferedReader(new StringReader(source));
    }

    public Scanner(FileInputStream source) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(source, StandardCharsets.UTF_8));
    }

    public boolean hasNextLine() throws IOException {
        return this.reader.ready();
    }

    public boolean hasNext() throws IOException {
        int buffer = 1;
        int counter = 0;
        this.reader.mark(buffer);
        int nowSymbCode = this.reader.read();
        while (nowSymbCode != -1) {
            counter++;
            if ((Character.getType(nowSymbCode) == Character.LOWERCASE_LETTER ||
                    Character.getType(nowSymbCode) == Character.DASH_PUNCTUATION ||
                    Character.getType(nowSymbCode) == Character.UPPERCASE_LETTER ||
                    nowSymbCode == '\'')) {
                this.reader.reset();
                return true;
            }
            if (counter == buffer) {
                counter = 1;
                buffer = buffer * 2;
                this.reader.reset();
                this.reader.mark(buffer);
            }
            nowSymbCode = this.reader.read();
        }
        this.reader.reset();
        return false;
    }

    public boolean hasNextAnyone() throws IOException {
        int buffer = 16;
        int counter = 0;
        this.reader.mark(buffer);
        int nowSymbCode = this.reader.read();
        while (nowSymbCode != -1) {
            counter++;
            char nowSymb = (char) nowSymbCode;
            if (!Character.isWhitespace(nowSymb)) {
                this.reader.reset();
                return true;
            }
            if (counter == buffer) {
                counter = 1;
                buffer = buffer * 2;
                this.reader.reset();
                this.reader.mark(buffer);
                this.reader.skip(buffer / 2 - 1); // for speed
                continue;
            }
            nowSymbCode = this.reader.read();
        }
        this.reader.reset();
        return false;
    }

    public String next() throws IOException {
        StringBuffer buf = new StringBuffer();
        boolean flag = false;
        this.reader.mark(1);
        int nowSymbCode = this.reader.read();
        while (nowSymbCode != -1) {
            //System.err.println(nowSymbCode);
            char nowSymb = (char) nowSymbCode;
            if (!(Character.getType(nowSymbCode) == Character.LOWERCASE_LETTER ||
                    Character.getType(nowSymbCode) == Character.DASH_PUNCTUATION ||
                    Character.getType(nowSymbCode) == Character.UPPERCASE_LETTER ||
                    nowSymbCode == '\'')) {
                if (flag == true) {
                    if ((char) nowSymbCode == separator.charAt(0)) {
                        this.reader.reset();
                    }
                    return buf.toString();
                }
            } else {
                flag = true;
                buf.append(nowSymb);
            }
            this.reader.mark(1);
            nowSymbCode = this.reader.read();
        }
        return buf.toString();
    }

    public String nextInt() throws IOException {
        StringBuffer buf = new StringBuffer();
        boolean flag = false;
        int nowSymbCode = this.reader.read();
        while (nowSymbCode != -1) {
            char nowSymb = (char) nowSymbCode;
            if (Character.isWhitespace(nowSymb)) {
                if (flag == true) {
                    if ((char) nowSymbCode == separator.charAt(0)) {
                        this.reader.reset();
                    }
                    return buf.toString();
                }
            } else {
                flag = true;
                buf.append(nowSymb);
            }
            nowSymbCode = this.reader.read();
        }
        return buf.toString();
    }

    public boolean isEnd() throws IOException {
        int buffer = 1;
        this.reader.mark(buffer);
        int nowSymbCode = this.reader.read();
        int counter = 1;
        boolean flag = false;
        while (nowSymbCode != -1) {
            if ((char) nowSymbCode == separator.charAt(0)) {
                boolean isSeparator = true;
                this.reader.reset();
                this.reader.mark(counter + separator.length());
                this.reader.skip(counter);
                for (int i = 1; i < separator.length(); i++) {
                    nowSymbCode = this.reader.read();
                    if (nowSymbCode == -1) {
                        this.reader.reset();
                        this.reader.mark(buffer);
                        this.reader.skip(counter);
                        isSeparator = true;
                        break;
                    }
                    if ((char) nowSymbCode != separator.charAt(i)) {
                        isSeparator = false;
                        break;
                    }
                }
                this.reader.reset();
                this.reader.mark(buffer);
                this.reader.skip(counter);
                if (isSeparator == true) {
                    return true;
                }
            }
            if ((Character.getType(nowSymbCode) == Character.LOWERCASE_LETTER ||
                    Character.getType(nowSymbCode) == Character.DASH_PUNCTUATION ||
                    Character.getType(nowSymbCode) == Character.UPPERCASE_LETTER ||
                    nowSymbCode == '\'')) {
                this.reader.reset();
                return false;
            }
            if (counter >= buffer) {
                counter = buffer - 1;
                buffer = buffer * 2;
                this.reader.reset();
                this.reader.mark(buffer);
                this.reader.skip(buffer / 2 - 1); // faster
            }
            nowSymbCode = this.reader.read();
            counter++;
        }
        this.reader.reset();
        return true;
    }

    public String nextLine() throws IOException {
        StringBuffer buf = new StringBuffer();
        boolean flag;
        int nowSymbCode = 0;
        while (nowSymbCode != -1) {
            this.reader.mark(separator.length());
            flag = false;
            for (int i = 0; i < separator.length(); i++) {
                nowSymbCode = this.reader.read();
                if (nowSymbCode == -1) {
                    flag = true;
                    break;
                }
                if (separator.charAt(i) != (char) nowSymbCode) {
                    flag = true;
                }
            }
            if (flag == false) {
                return buf.toString();
            }
            this.reader.reset();
            nowSymbCode = this.reader.read();
            char nowSymb = (char) nowSymbCode;
            buf.append(nowSymb);
        }
        return buf.toString();
    }
}