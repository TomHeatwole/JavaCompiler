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
            } else if (t.getValue().equals("(")) { // TODO: []
                type = ExpressionType.PARENS;
                value = t.getValue();
                return makeSingleChild(new Token(")", TokenType.SYMBOL), tokens, location);
            }             
        }
        if (tokens[location + 1].equals(terminalToken)) {
            this.type = Parser.TokenTypeToExpressionType.get(tokens[location].getType());
            if (type == null) {
                System.out.println(tokens[location]);
                System.out.println(terminalToken);
                return Parser.notifyInvalidGeneric(tokens[location]);
            }
            this.returnType = Parser.ExpressionTypeToReturnType.get(this.type);
            this.value = tokens[location].getValue();
            return location + 1;
        }
        
        // Check if binary expression
        int[] firstOpLocation = new int[Parser.orderOpsSize]; // indexed by order of operations defined here: https://introcs.cs.princeton.edu/java/11precedence/
        int i = location;
        for (; i < tokens.length && !tokens[i].equals(terminalToken); i++) {
            // TODO: skip over ()s and []s ****USE LEXER????*******
            Integer index = Parser.orderOfOperations.get(tokens[i]);
            if (index != null && firstOpLocation[index] == 0) {
                // TODO: This check will need to change once we accept ++ and -- 
                t = tokens[i];
                if (!t.getValue().equals("-") || i != location && tokens[i - 1].getType() != TokenType.SYMBOL) {
                    firstOpLocation[index] = i;
                }
            }
        }
        if (i == tokens.length) {
            return Parser.notifyInvalid("Expected '" + terminalToken.getValue() + "' but never found.", tokens[i - 2].getLineNumber());
        }
        for (int j = 0; j < firstOpLocation.length; j++) {
            if (firstOpLocation[j] != 0) {
                int opLoc = firstOpLocation[j];
                t = tokens[opLoc];
                type = ExpressionType.BINARY;
                binaryOperator = t.getValue();
                children = new Expression[2];
                children[0] = new Expression(this);
                ((Expression)(children[0])).setTerminalToken(t);
                children[0].populate(tokens, location);
                children[1] = new Expression(this);
                ((Expression)(children[1])).setTerminalToken(terminalToken);
                children[1].populate(tokens, opLoc + 1);
                if (binaryOperator.equals("+")) {
                    returnType = ((Expression)(children[0])).getReturnType().equals("String") ? "String" : "#";
                } else {
                    returnType = Parser.binOpToReturnType.get(binaryOperator);
                }
                if (returnType.equals("#")) {
                    if (((Expression)(children[0])).getReturnType().equals("int") && ((Expression)(children[1])).getReturnType().equals("int")) {
                        returnType = "int";
                    } else {
                        // TODO: Find out if it's necessary to differentiate between int/short/long and float/double here
                        returnType = "float";
                    }
                }
                return i;
            }
        }
        t = tokens[location];
        if (t.getType() == TokenType.SYMBOL && Parser.unaryOperators.contains(t.getValue())) {
            type = ExpressionType.UNARY;
            value = t.getValue();
            return makeSingleChild(terminalToken, tokens, location);
        }

        // TODO: Every other type of expression 
        System.out.println("Error: Not implemented");
        return -1;
    }

    private int makeSingleChild(Token childTerminal, Token[] tokens, int location) {
        children = new Expression[1];
        children[0] = new Expression(this);
        ((Expression)(children[0])).setTerminalToken(childTerminal);
        int ret = children[0].populate(tokens, ++location);
        this.returnType = ((Expression)(children[0])).getReturnType();
        return ret;
    };
}

