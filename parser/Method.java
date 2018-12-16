// TODO: another class (maybe abstract) for objectWithHeader

public class Method extends ItemWithHeader {

    private static Token terminalToken;

    static {
        terminalToken = new Token("}", TokenType.SYMBOL);
    }

    public Method(AbstractSyntaxTree parent) {
        super(parent);
    }

    public int populate(Token[] tokens, int location) {
        for (;;location++) {
            if (tokens[location].equals(terminalToken)) return ++location;
        }
    }
}
