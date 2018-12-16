import java.util.LinkedList;

public class Class extends AbstractSyntaxTree {

    private static Token terminalToken;
    private String accessModifier;
    private String type; // abstract/interface/class
    private boolean isAbstract;
    private String name;

    static {
        terminalToken = new Token("}", TokenType.SYMBOL);
    }

    public Class(AbstractSyntaxTree parent) {
        super(parent);
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

    public int populate(Token[] tokens, int location) {
        LinkedList<AbstractSyntaxTree> childrenList = new LinkedList<>();

        // prase header
        for (;;location++) {
            Token t = tokens[location];
            if (t.getType() != TokenType.KEYWORD) {
                return Parser.notifyInvalid("Invalid start of class", t.getLineNumber());
            }
            if (Parser.accessModifiers.contains(t.getValue())) {
                if (accessModifier != null) {
                    return Parser.notifyInvalid("Multiple accessModifier keywords used in class declaration", t.getLineNumber());
                }
                accessModifier = t.getValue();
            } else if (Parser.classTypes.contains(t.getValue())) {
                if (getType() != null) {
                    return Parser.notifyInvalid("Multiple class getType() keywords used in class declaration", t.getLineNumber());
                }
                type = t.getValue();
                break;
            } else if (t.getValue().equals("abstract")) {
                if (this.isAbstract) {
                    return Parser.notifyInvalid("Abstract defined twice in class declaration", t.getLineNumber());
                }
                this.isAbstract = true;
            }
        }

        // parse class name
        Token t = tokens[++location];
        if (t.getType() != TokenType.IDENTIFIER) {
            return Parser.notifyInvalid("Expected class name but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        this.name = t.getValue();

        // TODO: Accept extends and implements
        // parse class body
        t = tokens[++location];
        if (t.getType() != TokenType.SYMBOL || !t.getValue().equals("{")) {
            return Parser.notifyInvalid("Expected '{', but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        ++location;
        while (!tokens[location].equals(terminalToken)) {
            AbstractSyntaxTree child = new Class(null); // will get correctly populated inside parseHeader
            location = Parser.parseHeader(tokens, location, this, child);
            if (location == -1) {
                return -1;
            }
            childrenList.add(child);
        }
        children = new AbstractSyntaxTree[childrenList.size()];
        childrenList.toArray(children);
        return ++location;
    }
}

