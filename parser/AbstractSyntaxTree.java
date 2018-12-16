public abstract class AbstractSyntaxTree {

    private AbstractSyntaxTree[] children;
    private Token terminalToken;
    public boolean valid;

    public AbstractSyntaxTree(Token[] tokens, int location); 

    public AbstractSyntaxTree[] getChildren() {
        return this.children;
    }
}

