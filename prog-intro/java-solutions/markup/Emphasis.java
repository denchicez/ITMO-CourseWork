package markup;

import java.util.List;

public class Emphasis extends Out {
    public Emphasis(List<ToParse> listParse) {
        super(listParse, "*", "<em>", "</em>");
    }

    public void toMarkdown(StringBuilder builder) {
        super.toMarkdown(builder);
    }

    public void toHtml(StringBuilder builder) {
        super.toHtml(builder);
    }
}