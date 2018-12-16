public abstract class AbstractSyntaxTree {

    AbstractSyntaxTree[] children;
    AbstractSyntaxTree parent;

    public AbstractSyntaxTree(AbstractSyntaxTree parent){
        this.parent = parent;
    }

    abstract public int populate(Token[] tokens, int location);

    public AbstractSyntaxTree[] getChildren() {
        return this.children;
    }
}

