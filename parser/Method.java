// TODO: another class (maybe abstract) for objectWithHeader

public class Method extends ItemWithHeader {

    private static Token terminalToken;

    private boolean isMain;

    // TODO: Store params

    static {
        terminalToken = new Token("}", TokenType.SYMBOL);
    }

    public Method(AbstractSyntaxTree parent) {
        super(parent);
    }

    public boolean isMain() {
        return this.isMain;
    }

    // location should start on "("
    public int populate(Token[] tokens, int location) {
        Token t = tokens[location];
        if (t.getType() != TokenType.SYMBOL || !t.getValue().equals("(")) {
            return Parser.notifyInvalid("Expected '(' but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        // TODO: process param list
        t = tokens[++location];
        if (t.getType() != TokenType.SYMBOL || !t.getValue().equals(")")) {
            return Parser.notifyInvalid("Expected ')' but found \"" + t.getValue() + "\"", t.getLineNumber());
        }

        // Check if main method
        // TODO: also check for String[] param
        if (name.equals("main") && type.equals("void") && isStatic && accessModifier.equals("public")) {
            isMain = true;
        }

        // TODO: parse method body
        for (;;location++) if (tokens[location].getValue().equals("}")) return ++location;






    }
}
