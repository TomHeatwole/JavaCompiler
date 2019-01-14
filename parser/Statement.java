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
            System.out.println("Error: We only support \"return <int_literal>;\" right now.");
            return -1;
        }
        type = StatementType.RETURN;
        Token t = tokens[++location];
        if (t.getType() == TokenType.INT_LITERAL) {
            this.val = Integer.parseInt(t.getValue());
        } else {
            System.out.println("Error: We only support \"return <int_literal>;\" right now.");
            return -1;
        }
        return (tokens[++location].equals(terminalToken) ? ++location : -1);
    }
}

