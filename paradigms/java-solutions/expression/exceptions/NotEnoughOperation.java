package expression.exceptions;

public class NotEnoughOperation extends ParserException {
    public NotEnoughOperation() {
        super("not found enough operation");
    }
}
