import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

public class Compile {

	public static void main(String[] args) {
        // Check args
		if (args.length == 0) {
			System.out.println("Invalid number of parameters.");
			System.out.println("Usage: java Compile <name_of_java_file>");
			return;
		}

        // Lex
		final String keywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
				"const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float",
				"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "main", "native",
				"new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
				"super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
				"volatile", "while" };
        Lexer l = new Lexer(keywords);
		List<Token> tokens = l.lex(args[0]);
		if (tokens.size() == 0) {
			return;
		}
		for (int i = 0; i < tokens.size(); i++) {
			Token T = tokens.get(i);
			System.out.println(T.lineNumber + " " + T.type + " " + T.value);
		}
	}
}
