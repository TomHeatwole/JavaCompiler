public abstract class AbstractSyntaxTree {

    protected AbstractSyntaxTree[] children;
    protected AbstractSyntaxTree parent;

    public AbstractSyntaxTree(AbstractSyntaxTree parent){
        this.parent = parent;
        this.children = new AbstractSyntaxTree[0];
    }

    abstract public int populate(Token[] tokens, int location);

    public AbstractSyntaxTree[] getChildren() {
        return this.children;
    }

    public AbstractSyntaxTree getParent() {
        return this.parent;
    }

    // For Debugging
    public String toString() {
        String ret = "";
        for (int i = 0; i < children.length; i++) {
            ret += i + " " + children[i] + "\n";
        }
        return ret;
    }
}

