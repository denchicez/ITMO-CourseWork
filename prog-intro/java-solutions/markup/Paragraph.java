package markup;

import java.util.List;
import java.io.*;

interface ToParse {
    void toMarkdown(StringBuilder buildString);

    void toHtml(StringBuilder buildString);
}

abstract class Out implements ToParse {
    List<ToParse> listParse;
    String signMark;
    String firstHtml;
    String secondHtml;

    public Out(List<ToParse> listParse, String signMark, String firstHtml, String secondHtml) {
        this.listParse = listParse;
        this.signMark = signMark;
        this.firstHtml = firstHtml;
        this.secondHtml = secondHtml;
    }

    public void toMarkdown(StringBuilder builder) {
        builder.append(this.signMark);
        for (int i = 0; i < this.listParse.size(); i++) {
            ToParse out = this.listParse.get(i);
            out.toMarkdown(builder);
        }
        builder.append(this.signMark);
    }

    public void toHtml(StringBuilder builder) {
        builder.append(this.firstHtml);
        for (int i = 0; i < this.listParse.size(); i++) {
            ToParse out = this.listParse.get(i);
            out.toHtml(builder);
        }
        builder.append(this.secondHtml);
    }
}

public class Paragraph extends Out {
    public Paragraph(List list) {
        super(list, "", "", "");
    }

    public void toMarkdown(StringBuilder builder) {
        super.toMarkdown(builder);
    }

    public void toHtml(StringBuilder builder) {
        super.toHtml(builder);
    }
}