package markup;

import java.util.List;

public class Strikeout extends Out {
    public Strikeout(List<ToParse> listParse) {
        super(listParse, "~", "<s>", "</s>");
    }

    public void toMarkdown(StringBuilder builder) {
        super.toMarkdown(builder);
    }

    public void toHtml(StringBuilder builder) {
        super.toHtml(builder);
    }
}