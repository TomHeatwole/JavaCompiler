public class Expression extends AbstractSyntaxTree {

    private Token terminalToken;
    private string unaryOperator;
    private string[] binaryOperators;
    // todo: list of operators
    //
    public Statement(AbstractSyntaxTree parent) {
        super(parent);
        unaryOperator = "";
        binaryOperator = "";
        terminalToken = new Token(TokenType.SYMBOL, ";", 0);
    }

    public String getUnaryType() {
        return unaryOperator;
    }

    public binaryOperator[] getBinaryTypes() {
        return binaryOperators;
    }

    public void setTerminalToken(Token t) {
        terminalToken = t;
    }

    public int populate(Token[] tokens, int location) {
        Token t = tokens[location];
        if (t.getType() == TokenType.SYMBOL) {
            if (t == terminalToken) {
                // TODO: Set expression type as empty 
                return location;
            }
            children = new Expression[1];
            children[0] = new Expression(this);
            if (!t.getValue().equals("(")) {
                unaryOperator = t.getType();
                children = new Expression[1];
                children[0] = new Expression(this);
                return children[0].populate(tokens, ++location);
            }
            // TODO: set type as parentheses
            children[0].setTerminalToken(new Token(TokenType.SYMBOL, ")"));
            return children[0].populate(Tokens, ++location);
        }
        // TODO: Process binary expressions. initialize lists, loop
        return -1;
    }
}

