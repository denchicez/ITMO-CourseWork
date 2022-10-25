package markup;

import java.util.List;

public class Strong extends Out {
    public Strong(List<ToParse> listParse) {
        super(listParse, "__", "<strong>", "</strong>");
    }

    public void toMarkdown(StringBuilder builder) {
        super.toMarkdown(builder);
    }

    public void toHtml(StringBuilder builder) {
        super.toHtml(builder);
    }
}