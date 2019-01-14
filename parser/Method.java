public class Method extends ItemWithHeader {

    private boolean isMain;

    // TODO: Store params

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

        t = tokens[++location];
        if (t.getType() != TokenType.SYMBOL || (!t.getValue().equals("{") && !t.getValue().equals(";"))) {
            return Parser.notifyInvalidGeneric(t);
        }
        if (t.getValue().equals(";")) {
            children = new AbstractSyntaxTree[0];
            return ++location;
        }
        // else value = "{"
        children = new AbstractSyntaxTree[1];
        children[0] = new Block(this);
        return children[0].populate(tokens, ++location);
    }
}
