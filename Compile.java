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
				"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
				"new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
				"super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
				"volatile", "while" };
        Lexer l = new Lexer(javaKeywords);
		Token[] tokens = l.lex(args[0]);
		if (tokens.length == 0) {
			return;
		}
        // Parse
        Program p = new Program(null);
        if (p.populate(tokens, 0) == -1) {
            System.out.println("FAILED");
            return;
        }
        System.out.println(p);
	}
}

