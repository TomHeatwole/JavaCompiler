import java.util.LinkedList;

public class Block extends AbstractSyntaxTree {

    private static Token terminalToken;

    static {
        terminalToken = new Token("}", TokenType.SYMBOL);
    }

    public Block(AbstractSyntaxTree parent) {
        super(parent);
    }

    public int populate(Token[] tokens, int location) {
        LinkedList<AbstractSyntaxTree> childrenList = new LinkedList<>();
        while (!tokens[location].equals(terminalToken)) {
            Statement s = new Statement(this);
            location = s.populate(tokens, location);
            if (location < 0) {
                return -1;
            }
            childrenList.add(s);
        }
        children = new AbstractSyntaxTree[childrenList.size()];
        childrenList.toArray(children);
        return ++location;
    }
}


