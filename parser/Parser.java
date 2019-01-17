// Utility variables and methods to assist with parsing

import java.util.HashSet;
import java.util.HashMap;

public class Parser {
    
    public static HashSet<String> accessModifiers;
    public static HashSet<String> classTypes;
    public static HashSet<String> primitiveTypes;
    public static HashSet<String> customTypes;
    public static HashSet<String> unaryOperators;
    public static HashMap<TokenType, ExpressionType> TokenTypeToExpressionType;
    public static HashMap<ExpressionType, String> ExpressionTypeToReturnType;
    public static HashMap<Token, Integer> orderOfOperations;
    public static HashMap<String, String> binOpToReturnType;

    public static final int orderOpsSize = 17;

    // TODO: keep track of class variables HashSet[class & name & params] 

    static {
        accessModifiers = new HashSet<>();
        classTypes = new HashSet<>();
        primitiveTypes = new HashSet<>();
        customTypes = new HashSet<>();
        unaryOperators = new HashSet<>();
        TokenTypeToExpressionType = new HashMap<>();
        ExpressionTypeToReturnType = new HashMap<>();
        orderOfOperations = new HashMap<>();
        binOpToReturnType = new HashMap<>();

        accessModifiers.add("public");
        accessModifiers.add("private");
        accessModifiers.add("protected");
        // TODO: Support internal?
        
        classTypes.add("class");
        classTypes.add("interface");

        primitiveTypes.add("byte");
        primitiveTypes.add("short");
        primitiveTypes.add("int");
        primitiveTypes.add("long");
        primitiveTypes.add("char");
        primitiveTypes.add("float");
        primitiveTypes.add("double");
        primitiveTypes.add("boolean");

        // TODO actually implement customTypes with access modifier settings
        
        unaryOperators.add("++");
        unaryOperators.add("--");
        unaryOperators.add("~");
        unaryOperators.add("-");
        unaryOperators.add("!");

        TokenTypeToExpressionType.put(TokenType.STRING_LITERAL, ExpressionType.STRING_LITERAL);
        TokenTypeToExpressionType.put(TokenType.INT_LITERAL, ExpressionType.INT_LITERAL);
        TokenTypeToExpressionType.put(TokenType.CHAR_LITERAL, ExpressionType.CHAR_LITERAL);
        TokenTypeToExpressionType.put(TokenType.FLOAT_LITERAL, ExpressionType.FLOAT_LITERAL);

        ExpressionTypeToReturnType.put(ExpressionType.STRING_LITERAL, "String");
        ExpressionTypeToReturnType.put(ExpressionType.CHAR_LITERAL, "char");
        ExpressionTypeToReturnType.put(ExpressionType.INT_LITERAL, "int");
        ExpressionTypeToReturnType.put(ExpressionType.FLOAT_LITERAL, "float");

        // TODO: Populate as much as necessary from: https://introcs.cs.princeton.edu/java/11precedence/
        orderOfOperations.put(new Token("*", TokenType.SYMBOL), 12);
        orderOfOperations.put(new Token("/", TokenType.SYMBOL), 12);
        orderOfOperations.put(new Token("%", TokenType.SYMBOL), 12);
        orderOfOperations.put(new Token("+", TokenType.SYMBOL), 11);
        orderOfOperations.put(new Token("-", TokenType.SYMBOL), 11);
        orderOfOperations.put(new Token(">>", TokenType.SYMBOL), 10);
        orderOfOperations.put(new Token("<<", TokenType.SYMBOL), 10);
        orderOfOperations.put(new Token(">>>", TokenType.SYMBOL), 10); // Still needs to be written into lexer
        orderOfOperations.put(new Token(">", TokenType.SYMBOL), 9);
        orderOfOperations.put(new Token("<", TokenType.SYMBOL), 9);
        orderOfOperations.put(new Token("<=", TokenType.SYMBOL), 9);
        orderOfOperations.put(new Token(">=", TokenType.SYMBOL), 9);
        orderOfOperations.put(new Token("instanceof", TokenType.KEYWORD, 0), 9);
        orderOfOperations.put(new Token("==", TokenType.SYMBOL), 8);
        orderOfOperations.put(new Token("!=", TokenType.SYMBOL), 8);
        orderOfOperations.put(new Token("&", TokenType.SYMBOL), 7);
        orderOfOperations.put(new Token("^", TokenType.SYMBOL), 6);
        orderOfOperations.put(new Token("|", TokenType.SYMBOL), 5);
        orderOfOperations.put(new Token("&&", TokenType.SYMBOL), 4);
        orderOfOperations.put(new Token("||", TokenType.SYMBOL), 3);

        binOpToReturnType.put("*", "#");
        binOpToReturnType.put("/", "#");
        binOpToReturnType.put("%", "#");
        binOpToReturnType.put("-", "#");
        binOpToReturnType.put(">>", "#");
        binOpToReturnType.put("<<", "#");
        binOpToReturnType.put(">>>", "#");
        binOpToReturnType.put("&", "#");
        binOpToReturnType.put("^", "#");
        binOpToReturnType.put("|", "#");

        binOpToReturnType.put("<", "boolean");
        binOpToReturnType.put(">", "boolean");
        binOpToReturnType.put("<=", "boolean");
        binOpToReturnType.put(">=", "boolean");
        binOpToReturnType.put("==", "boolean");
        binOpToReturnType.put("!=", "boolean");
        binOpToReturnType.put("instanceof", "boolean");
        binOpToReturnType.put("&&", "boolean");
        binOpToReturnType.put("||", "boolean");
    }

    public static int notifyInvalid(String message, int lineNumber) {
        System.out.println("Error: " + message + " on line " + lineNumber);
        return -1;
    }

    public static int notifyInvalidGeneric(Token t) {
        return notifyInvalid("Unexpected token: '" + t.getValue() + "'", t.getLineNumber());
    }

    public static int parseHeader(Token[] tokens, int location, AbstractSyntaxTree parent, ItemWithHeader[] child) {
        String accessModifier = "";
        boolean isAbstract = false;
        boolean isStatic = false;
        for (;;location++) {
            Token t = tokens[location];
            if (t.getType() != TokenType.KEYWORD) {
                if (t.getType() != TokenType.IDENTIFIER) {
                    return Parser.notifyInvalid("Invalid start of header", t.getLineNumber());
                }
                if (!Parser.customTypes.contains(t.getValue())) {
                    return Parser.notifyInvalid("Unrecognized type: \"" + t.getValue() + "\"", t.getLineNumber());
                }
                child[0] = new Method(parent);
                child[0].setType(t.getValue());
                break;
            }
            if (Parser.accessModifiers.contains(t.getValue())) {
                if (!accessModifier.equals("")) {
                    return Parser.notifyInvalid("Multiple accessModifier keywords used in header", t.getLineNumber());
                }
                accessModifier = t.getValue();
            } else if (t.getValue().equals("abstract")) {
                if (isAbstract) {
                    return Parser.notifyInvalid("Abstract defined twice in header", t.getLineNumber());
                }
                isAbstract = true;
            } else if (t.getValue().equals("static")) {
                if (isStatic) {
                    return Parser.notifyInvalid("Static defined twice in header", t.getLineNumber());
                }
                isStatic = true;
            } else if (Parser.classTypes.contains(t.getValue())) {
                child[0] = new Class(parent);
                child[0].setType(t.getValue());
                break;
            } else if (Parser.primitiveTypes.contains(t.getValue()) || t.getValue().equals("void")) {
                child[0] = new Method(parent);
                child[0].setType(t.getValue());
                break;
            } else {
                return Parser.notifyInvalidGeneric(t);
            }
        }
        child[0].setAccessModifier(accessModifier);
        child[0].setAbstract(isAbstract);
        child[0].setStatic(isStatic);
        // parse name
        Token t = tokens[++location];
        if (t.getType() != TokenType.IDENTIFIER) {
            return Parser.notifyInvalid("Expected identifier but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        child[0].setName(t.getValue());
        return child[0].populate(tokens, ++location);
    }
}

