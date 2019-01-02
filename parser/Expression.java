public class Expression extends AbstractSyntaxTree {

    private Token terminalToken;
    private UnaryOperatorType unaryType;
    // todo: list of operators

    public UnaryOperatorType getUnaryType() {
        return uTYpe;
    }

    public Token setTerminalToken(Token t) {
        this.terminalToken = t;
    }

    public int populate(Token[] tokens, int location) {
        
    }
}

