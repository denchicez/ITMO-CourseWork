package expression.exceptions;

public class IncorrectBrackets extends ParserException {
    public IncorrectBrackets() {
        super("incorrect bracket sequence");
    }
}
