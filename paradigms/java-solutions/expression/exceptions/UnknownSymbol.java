package expression.exceptions;

public class UnknownSymbol extends ParserException {
    public UnknownSymbol() {
        super("unkown this symbol");
    }
}
