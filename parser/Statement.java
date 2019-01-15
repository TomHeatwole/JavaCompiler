public class Statement extends AbstractSyntaxTree {

    private static Token terminalToken;
    private StatementType type;
    private int val; // TODO: Remove

    static {
        terminalToken = new Token(";", TokenType.SYMBOL);
    }

    public Statement(AbstractSyntaxTree parent) {
        super(parent);
    }

    public StatementType getType() {
        return type;
    }

    // TODO: Remove
    public int getValue() {
        return val;
    }

    public int populate(Token[] tokens, int location) {
        // Examples:
        // Return <exp>
        // keyword name = <exp>
        // name = <exp>
        // name.name().name = <exp>
        // int name
        // if (cond) <exp>
        // <exp>
        Token r = new Token("return", TokenType.KEYWORD);
        if (!tokens[location].equals(r)) {
            System.out.println("Error: We only support \"return <expression>;\" right now.");
            return -1;
        }
        // TODO: Handle return from void
        type = StatementType.RETURN;
        children = new Expression[1];
        children[0] = new Expression(this);
        location = children[0].populate(tokens, ++location);
        // TODO: Check children[0].getReturnType() is valid to return from method type (maybe do in gen)
        return location > -1 && tokens[location].equals(terminalToken) ? ++location : -1;
    }
}

