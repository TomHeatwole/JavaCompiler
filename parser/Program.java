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
        //ItemWithHeader c = new Class(null); // will get set correctly by parseHeader
        ItemWithHeader[] child = new ItemWithHeader[1];
        int next = Parser.parseHeader(tokens, 0, parent, child);
        children = new AbstractSyntaxTree[1];
        children[0] = child[0];
        //TODO: support another class in the same file
        return (next != -1 && tokens[next].equals(terminalToken)) ? ++next : -1;
    }

}

