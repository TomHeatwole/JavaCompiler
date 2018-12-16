// Utility variables and methods to assist with parsing

import java.util.HashSet;

public class Parser {
    
    public static HashSet<String> accessModifiers;
    public static HashSet<String> classTypes;
    public static HashSet<String> primitiveTypes;
    // TODO: keep track of non-primitive types and update as we parse class types

    static {
        accessModifiers = new HashSet<String>();
        classTypes = new HashSet<String>();
        primitiveTypes = new HashSet<String>();

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
    }

    public static int notifyInvalid(String message, int lineNumber) {
        System.out.println("Error: " + message + " on line " + lineNumber);
        return -1;
    }

    public static int parseHeader(Token[] tokens, int location, AbstractSyntaxTree parent, AbstractSyntaxTree child) {
        // TODO: actually implement
        child = new Method(parent);
        return child.populate(tokens, location);
    }
}

