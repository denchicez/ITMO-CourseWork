package md2html;

public class Tags {
    int openIndex;
    String openTag;
    String closeTag;

    public Tags(int openIndex, String openTag, String closeTag) {
        this.openIndex = openIndex;
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    public int getOpenIndex() {
        return this.openIndex;
    }

    public void replaceOpenIndex(int newOpen) {
        this.openIndex = newOpen;
    }
}
