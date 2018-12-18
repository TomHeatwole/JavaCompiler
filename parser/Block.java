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
        /*
         * TODO: Parse list of statements
        LinkedList<AbstractSyntaxTree> childrenList = new LinkedList<>();

        while (!location.equals
        */
        for (;;location++) if (tokens[location].equals(terminalToken)) return ++location;
    }
}


