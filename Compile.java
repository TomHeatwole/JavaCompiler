import java.util.List;

public class Compile {

	public static void main(String[] args) {
        // Check args
		if (args.length == 0) {
			System.out.println("Invalid number of parameters.");
			System.out.println("Usage: java Compile <name_of_java_file>");
			return;
		}

        // Lex
		final String javaKeywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
				"const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float",
				"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "main", "native",
				"new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
				"super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
				"volatile", "while" };
        Lexer l = new Lexer(javaKeywords);
		List<Token> tokens = l.lex(args[0]);
		if (tokens.size() == 0) {
			return;
		}
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            System.out.println(t.lineNumber +  " " + t.type + " " + t.value);
        }

        // Parse
        // TODO
	}
}
