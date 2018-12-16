public abstract class AbstractSyntaxTree {

    protected AbstractSyntaxTree[] children;
    protected AbstractSyntaxTree parent;

    public AbstractSyntaxTree(AbstractSyntaxTree parent){
        this.parent = parent;
    }

    abstract public int populate(Token[] tokens, int location);

    public AbstractSyntaxTree[] getChildren() {
        return this.children;
    }

    public AbstractSyntaxTree getParent() {
        return this.parent;
    }
}

