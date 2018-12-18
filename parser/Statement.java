public class Statement extends AbstractSyntaxTree {

    private static Token terminalToken;
    public int val;

    static {
        terminalToken = new Token(";", TokenType.SYMBOL);
    }

    public Statement(AbstractSyntaxTree parent) {
        super(parent);
    }

    public int populate(Token[] tokens, int location) {
        // TODO: Way more parsing, make a parseStatement in Parser, maybe ditch this class completely and
        // have classes for different types of statements like we did for itemWithHeader. Also statement
        // might not be the best word since I'm thinking loops fall under this category
        System.out.println(tokens[location].getValue());
        if (!tokens[location].equals(r)) {
            return -1;
        }
        Token t = tokens[++location];
        if (t.getType() == TokenType.INT_LITERAL) {
            this.val = Integer.parseInt(t.getValue());
        } else return -1;
        return (tokens[++location].equals(terminalToken) ? ++location : -1);
    }
}
