// Utility variables and methods to assist with parsing

import java.util.HashSet;

public class Parser {
    
    public static HashSet<String> accessModifiers;
    public static HashSet<String> classTypes;
    public static HashSet<String> primitiveTypes;
    public static HashSet<String> customTypes;
    // TODO: keep track of class variables HashSet[class & name & params] 

    static {
        accessModifiers = new HashSet<String>();
        classTypes = new HashSet<String>();
        primitiveTypes = new HashSet<String>();
        customTypes = new HashSet<String>();

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
    }

    public static int notifyInvalid(String message, int lineNumber) {
        System.out.println("Error: " + message + " on line " + lineNumber);
        return -1;
    }

    public static int parseHeader(Token[] tokens, int location, AbstractSyntaxTree parent, ItemWithHeader child) {
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
                child = new Method(parent);
                child.setType(t.getValue());
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
                child = new Class(parent);
                child.setType(t.getValue());
                break;
            } else if (Parser.primitiveTypes.contains(t.getValue()) || t.getValue().equals("void")) {
                child = new Method(parent);
                child.setType(t.getValue());
                break;
            } else {
                return Parser.notifyInvalid("Unexpected token: \"" + t.getValue() + "\"", t.getLineNumber());
            }
        }
        child.setAccessModifier(accessModifier);
        child.setAbstract(isAbstract);
        child.setStatic(isStatic);
        // parse name
        Token t = tokens[++location];
        if (t.getType() != TokenType.IDENTIFIER) {
            return Parser.notifyInvalid("Expected identifier but found \"" + t.getValue() + "\"", t.getLineNumber());
        }
        child.setName(t.getValue());

        return child.populate(tokens, ++location);
    }
}

