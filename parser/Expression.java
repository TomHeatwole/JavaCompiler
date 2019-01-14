import java.util.LinkedList;

public class Expression extends AbstractSyntaxTree {

    private Token terminalToken;
    private String unaryOperator;
    private String[] binaryOperators;
    private ExpressionType type;
    private String returnType;

    public Expression(AbstractSyntaxTree parent) {
        super(parent);
        unaryOperator = "";
        binaryOperators = new String[0];
        terminalToken = new Token(";", TokenType.SYMBOL, 0);
    }

    public String getUnaryType() {
        return unaryOperator;
    }

    public String[] getBinaryTypes() {
        return binaryOperators;
    }

    public String getReturnType() {
        return returnType;
    }

    public ExpressionType getType() {
        return type;
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
                unaryOperator = t.getValue();
                ((Expression)(children[0])).setTerminalToken(this.terminalToken);
                return children[0].populate(tokens, ++location);
            } else if (t.getValue().equals("(")) {
                type = ExpressionType.PARENS;
                ((Expression)(children[0])).setTerminalToken(new Token(")", TokenType.SYMBOL));
            } else {
                // TODO: Figure out if there are any valid cases remaining
                return Parser.notifyInvalidGeneric(tokens[location]);
            }
            location = (children[0]).populate(tokens, ++location);
            // TODO: Check that return types make sense on unary operator (or should we do this during gen?)
            returnType = ((Expression)(children[0])).getReturnType();
            return location;
        }
        location++;
        if (tokens[location + 1].equals(terminalToken)) {
            this.type = Parser.TokenTypeToExpressionType.get(tokens[location].getType());
            if (type == null) {
                return Parser.notifyInvalidGeneric(tokens[location]);
            }
            this.returnType = Parser.ExpressionTypeToReturnType.get(this.type);
            return location + 1;
        }
        // TODO: Handle all binary stuff
        /* Consider first pass to find where to split and parse out parentheticals???
        // TODO: Doesn't have to be binary until we find a binary operator
        type = ExpressionType.BINARY;
        LinkedList<String> binaryList = new LinkedList<>();
        LinkedList<Expression> childrenList = new LinkedList<>();
        do {
            // TODO: Check end of binary expression
            Expression next = new Expression(this);
            // TODO: + 1 doesn't work since they're not all necessarily literals
            next.setTerminalToken(tokens[location + 1]);
            if (next.populate(tokens, location) != location + 1) {
                return -1;
            }
            location++;
            childrenList.add(next);
            binaryList.add(tokens[location + 1].getValue());
        } while (!tokens[location + 1].equals(terminalToken));
        */
        return -1;
    }
}

