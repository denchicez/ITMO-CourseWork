package markup;

import java.util.List;

public class Text implements ToParse {
    String out;

    public Text(String in) {
        this.out = in;
    }

    public void toMarkdown(StringBuilder builder) {
        builder.append(this.out);
    }

    public void toHtml(StringBuilder builder) {
        builder.append(this.out);
    }
}