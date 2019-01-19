import java.util.LinkedList;
import java.util.HashMap;

public class Expression extends AbstractSyntaxTree {

    private Token terminalToken;
    private ExpressionType type;
    private String returnType;
    private String value;
    private int endIndex; // only necessary when using alreadyParsed

    public Expression(AbstractSyntaxTree parent) {
        super(parent);
        value = "";
        returnType = "";
        terminalToken = new Token(";", TokenType.SYMBOL, 0);
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

    public int getEndIndex() {
        return endIndex;
    }

    public void setParent(Expression parent) {
        this.parent = parent;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void setTerminalToken(Token t) {
        terminalToken = t;
    }

    public int populate(Token[] tokens, int location) {
        return populate(tokens, location, new HashMap<Integer, Expression>());
    }

    protected int populate(Token[] tokens, int location, HashMap<Integer, Expression> alreadyParsed) {
        // TODO: Check for comma separated list of expressions
        Token t = tokens[location];
        if (tokens[location + 1].equals(terminalToken)) {
            this.type = Parser.TokenTypeToExpressionType.get(t.getType());
            if (type == null) {
                return Parser.notifyInvalidGeneric(t);
            }
            this.returnType = Parser.ExpressionTypeToReturnType.get(this.type);
            this.value = t.getValue();
            return location + 1;
        }
        
        // Check if binary expression
        int[] lastOpLocation = new int[Parser.orderOpsSize]; // indexed by order of operations defined here: https://introcs.cs.princeton.edu/java/11precedence/
        int i = location;
        for (; i < tokens.length && !tokens[i].equals(terminalToken); i++) {
            t = tokens[i];
            if (t.getValue().equals("(") || t.getValue().equals("[")) {
                Expression[] exp = new Expression[1];
                exp[0] = new Expression(this); // parent will be overridden if necessary
                String terminalString = t.getValue().equals("(") ? ")" : "]";
                exp[0].setTerminalToken(new Token(terminalString, TokenType.SYMBOL));
                i = populateExpression(exp, 0, tokens, i + 1, alreadyParsed);
                if (i == -1) {
                    return -1;
                }
                continue;
            }
            Integer strength = Parser.orderOfOperations.get(tokens[i]);
            if (strength != null) {
                // TODO: This check will need to change once we accept ++ and -- 
                if (!t.getValue().equals("-") || i != location && tokens[i - 1].getType() != TokenType.SYMBOL) {
                    lastOpLocation[strength] = i;
                } else {
                }
            }
        }
        if (i == tokens.length) {
            return Parser.notifyInvalid("Expected '" + terminalToken.getValue() + "' but never found.", tokens[i - 2].getLineNumber());
        }
        for (int j = 0; j < lastOpLocation.length; j++) {
            if (lastOpLocation[j] != 0) {
                int opLoc = lastOpLocation[j];
                t = tokens[opLoc];
                type = ExpressionType.BINARY;
                value = t.getValue();
                children = new Expression[2];
                children[0] = new Expression(this);
                ((Expression)(children[0])).setTerminalToken(t);
                populateExpression(children, 0, tokens, location, alreadyParsed);
                children[1] = new Expression(this);
                ((Expression)(children[1])).setTerminalToken(terminalToken);
                populateExpression(children, 1, tokens, opLoc + 1, alreadyParsed);
                if (value.equals("+")) {
                    returnType = ((Expression)(children[0])).getReturnType().equals("String") ? "String" : "#";
                } else {
                    returnType = Parser.binOpToReturnType.get(value);
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
        // i is termianl token, location is beginning
        if (alreadyParsed.containsKey(location)) {
            selfDeepCopy(alreadyParsed.get(location));
            return this.endIndex + 1;
        }
        t = tokens[location];
        value = t.getValue(); // will get reset if incorrect and necessary
        if (t.getType() == TokenType.SYMBOL) {
            if (t == terminalToken) {
                type = ExpressionType.EMPTY;
                return location;
            } else if (t.getValue().equals("(")) { // TODO: []
                type = ExpressionType.PARENS;
                location = makeSingleChild(new Token(")", TokenType.SYMBOL), tokens, location, alreadyParsed);
            } else if (t.getValue().equals("[")) {
                type = ExpressionType.BRACKETS;
                location = makeSingleChild(new Token("]", TokenType.SYMBOL), tokens, location, alreadyParsed);
            } else { // assuming unary for now
                type = ExpressionType.UNARY;
                return makeSingleChild(terminalToken, tokens, location, alreadyParsed);
            }
            return location;
        }

        // TODO: Every other type of expression 
        System.out.println("Error: Not implemented");
        return -1;
    }

    private int populateExpression(AbstractSyntaxTree[] toPopulate, int popIndex, Token[] tokens, int location, HashMap<Integer, Expression> alreadyParsed) {
        Expression exp = alreadyParsed.get(location);
        if (exp == null) {
            int ret = ((Expression)(toPopulate[popIndex])).populate(tokens, location, alreadyParsed);
            ((Expression)(toPopulate[popIndex])).setEndIndex(ret);
            alreadyParsed.put(location, (Expression)(toPopulate[popIndex]));
        } else {
            toPopulate[popIndex] = exp;
        }
        ((Expression)(toPopulate[popIndex])).setParent(this);
        return ((Expression)(toPopulate[popIndex])).getEndIndex();
    }

    private void selfDeepCopy(Expression copy) {
        type = copy.getType();
        returnType = copy.getReturnType();
        value = copy.getValue();
        endIndex = copy.getEndIndex();
        children = copy.getChildren();
    }

    private int makeSingleChild(Token childTerminal, Token[] tokens, int location, HashMap<Integer, Expression> alreadyParsed) {
        children = new Expression[1];
        children[0] = new Expression(this);
        ((Expression)(children[0])).setTerminalToken(childTerminal);
        int ret = populateExpression(children, 0, tokens, location + 1, alreadyParsed);
        this.returnType = ((Expression)(children[0])).getReturnType();
        return ret;
    }
}

