import java.util.LinkedList;

public class Class extends ItemWithHeader {

    private static Token terminalToken;
    private String accessModifier;
    private String type; // abstract/interface/class
    private boolean isAbstract;
    private String name;
    private int mainMethodIndex;

    static {
        terminalToken = new Token("}", TokenType.SYMBOL);
    }

    public Class(AbstractSyntaxTree parent) {
        super(parent);
        this.mainMethodIndex = -1;
    }

    public String getPrivacy() {
        return this.accessModifier;
    }

    public String getType() {
        return this.type;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasMain() {
        return this.mainMethodIndex > -1;
    }

    // index in children[] of main method or -1 if it doesn't exist
    public int getMainIndex() {
        return this.mainMethodIndex;
    }

    // location should start on {
    public int populate(Token[] tokens, int location) {
        LinkedList<AbstractSyntaxTree> childrenList = new LinkedList<>();

        // TODO: Accept extends and implements
        // parse class body
        Token t = tokens[location];
        if (t.getType() != TokenType.SYMBOL || !t.getValue().equals("{")) {
            return Parser.notifyInvalid("Expected '{', but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        ++location;
        while (!tokens[location].equals(terminalToken)) {
            ItemWithHeader child = new Class(null); // will get correctly populated inside parseHeader
            location = Parser.parseHeader(tokens, location, this, child);
            if (location == -1) {
                return -1;
            }
            childrenList.add(child);
            if (child instanceof Method) {
                Method m = (Method)child;
                if (m.isMain()) {
                    mainMethodIndex = childrenList.size() - 1;
                }
            }
        }
        children = new AbstractSyntaxTree[childrenList.size()];
        childrenList.toArray(children);
        return ++location;
    }
}

