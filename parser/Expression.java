import java.util.LinkedList;

public class Expression extends AbstractSyntaxTree {

    private Token terminalToken;
    private String binaryOperator;
    private ExpressionType type;
    private String returnType;
    private String value;

    public Expression(AbstractSyntaxTree parent) {
        super(parent);
        value = "";
        returnType = "";
        binaryOperator = "";
        terminalToken = new Token(";", TokenType.SYMBOL, 0);
    }

    public String getBinaryOperator() {
        return binaryOperator;
    }

    public String getReturnType() {
        return returnType;
    }

    public ExpressionType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setTerminalToken(Token t) {
        terminalToken = t;
    }

    public int populate(Token[] tokens, int location) {
        // TODO: Check for comma separated list of expressions
        Token t = tokens[location];
        if (t.getType() == TokenType.SYMBOL) {
            if (t == terminalToken) {
                type = ExpressionType.EMPTY;
                return location;
            }
            children = new Expression[1];
            children[0] = new Expression(this);
            if (Parser.unaryOperators.contains(t.getValue())) {
                type = ExpressionType.UNARY;
                value = t.getValue();
                ((Expression)(children[0])).setTerminalToken(this.terminalToken);
                location = children[0].populate(tokens, ++location);
                if (location == -1) {
                    return -1;
                }
                this.returnType = ((Expression)(children[0])).getReturnType();
                return location;
            } else if (t.getValue().equals("(")) { // TODO: []
                type = ExpressionType.PARENS;
                ((Expression)(children[0])).setTerminalToken(new Token(")", TokenType.SYMBOL));
            } else {
                // TODO: Figure out if there are any valid cases remaining
                return Parser.notifyInvalidGeneric(tokens[location]);
            }
            location = (children[0]).populate(tokens, ++location);
            this.returnType = ((Expression)(children[0])).getReturnType();
            return location;
        }
        if (tokens[location + 1].equals(terminalToken)) {
            this.type = Parser.TokenTypeToExpressionType.get(tokens[location].getType());
            if (type == null) {
                return Parser.notifyInvalidGeneric(tokens[location]);
            }
            this.returnType = Parser.ExpressionTypeToReturnType.get(this.type);
            this.value = tokens[location].getValue();
            return location + 1;
        }
        // TODO: For now we're assuming it's a binary operator. Also handle normal expressions.
        int startingLocation = location;
        int[] firstOpLocation = new int[17]; // indexed by order of operations defined here: https://introcs.cs.princeton.edu/java/11precedence/
        System.out.println("Error: not implemented");
        return -1;
    }
}

