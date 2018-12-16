public class Program extends AbstractSyntaxTree {
    
    private static Token terminalToken;

    static {
        terminalToken = new Token("", TokenType.EOF);
    }

    public Program(AbstractSyntaxTree parent) {
        super(parent);
    }

    public int populate(Token[] tokens, int location) {
        // TODO: Allow imports before class declaration
        Class c = new Class(this);
        int next = c.populate(tokens, 0);
        children = new AbstractSyntaxTree[1];
        children[0] = c;
        return (next != -1 && tokens[next].equals(terminalToken)) ? ++next : -1;
    }

}

