import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;


public class Compile {

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Invalid number of parameters.");
            System.out.println("Usage: java Compile <name_of_java_file>");
            return;
        }
        ArrayList<String> code = new ArrayList<>();
        ArrayList<Integer> lineNums = new ArrayList<>();
        if (!checkBalance(getScanner(args[0]), code, lineNums)) {
            return;
        }
        List<Token> tokens = lex(code, lineNums);
        if (tokens.size() == 0) {
            return;
        }
   }

   public static Scanner getScanner(String filename) {
        try {
            return new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.println("File: " + filename + " not found.");
        }
        return null;
   }

   // Checks for balanced parentheses and quotes: [], {}, () ""
   // TODO: Parse out comments and store result in code
    public static boolean checkBalance(Scanner s, List<String> code, List<Integer> lineNums) {
        if (s == null) {
            return false;
        }
        boolean quote = false;
        boolean comment = false;
        Stack<Character> parens = new Stack<>();
        Stack<String> locations = new Stack<>();
        String line = "";
        for (int lineNumber = 0; s.hasNextLine(); lineNumber++, line = ' ' + s.nextLine() + ' ') {
            StringBuilder parsedLine = new StringBuilder();
            for (int charNumber = 1; charNumber < line.length() - 1; charNumber++) {
                char c = line.charAt(charNumber);
                char prev = line.charAt(charNumber - 1);
                if (comment) {
                    if (c == '/' && prev == '*') {
                        comment = false;
                    }
                    continue;
                }
                if (prev == '\'' && line.charAt(charNumber + 1) == '\'') {} // character is a char literal
                else if (c == '"' && prev != '\\') {
                    quote = !quote;
                } else if (quote) {}
                else if (c == '/' && prev == '/') {
                    parsedLine.deleteCharAt(parsedLine.length() - 1);
                    break;
                } else if (c == '*' && prev == '/') {
                    parsedLine.deleteCharAt(parsedLine.length() - 1);
                    comment = true;
                    continue;
                } else if (c == '{' || c == '[' || c == '(') {
                    parens.push(c);
                    locations.push(lineNumber + ":" + charNumber);
                } else if (c == '}' || c == ']' || c == ')') {
                    if (parens.size() == 0) {
                        System.out.println("Error: Found unexpected '" + c + "' at "
                                + lineNumber + ":" + charNumber);
                        return false;
                    }
                    char paren = parens.pop();
                    if (Math.abs(c - paren) > 2) { // unbalanced case
                        System.out.println("Error: '" + paren + "' opened at " + locations.pop() 
                                + " closed with '" + c + "' at " + lineNumber + ":" + charNumber);
                        return false;
                    }
                    locations.pop();
                }
                parsedLine.append(c);
            }
            if (quote) {
                System.out.println("Error: Unclosed quotation mark on line " + lineNumber);
                return false;
            }
            String codeLine = parsedLine.toString().trim();
            if (codeLine.length() > 0) {
                code.add(parsedLine.toString());
                lineNums.add(lineNumber);
            }
        }
        if (parens.size() != 0) {
            System.out.println("Error: Unclosed '" + parens.pop() + "' opened at " + locations.pop());
            return false;
        }
        return true;
    }

    public static List<Token> lex(List<String> code, List<Integer> lineNums) {
        ArrayList<Token> ret = new ArrayList<>();
        final String keywords[] = {
            "abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "main", "native",
            "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while"
        };
        for (int i = 0; i < code.size(); i++) {
           String line = code.get(i);
           String[] words = line.split(" ");
           String word = "";
           boolean next = true;
           for (int j = 0; j < words.length; j++) {
               if (next) {
                   word = words[j];
               }
               next = true;
               if (word.length() == 0) {
                   continue;
               }
               if (Arrays.binarySearch(keywords, word) > 0) {
                   ret.add(new Token(word, TokenType.KEYWORD, lineNums.get(i)));
               } else if (Character.isDigit(word.charAt(0))) {
                   boolean decimal = false;
                   for (int k = 1; k < word.length(); k++) {
                       if (Character.isDigit(word.charAt(k))) {
                           continue;
                       }
                       if (word.charAt(k) == '.') {
                           if (decimal) {
                               notifyInvalidToken(word, lineNums.get(i), line.indexOf(word));
                           }
                           decimal = true;
                       } else if (Character.isLetter(word.charAt(k))) {
                           notifyInvalidToken(word, lineNums.get(i), line.indexOf(word));
                       } else {
                           ret.add(new Token(word.substring(0, k), TokenType.IDENTIFIER, lineNums.get(i)));
                           word = word.substring(k);
                           next = false;
                           break;
                       }
                   }
                   ret.add(new Token(word, decimal ? TokenType.FLOAT_LITERAL : TokenType.INT_LITERAL, lineNums.get(i)));
               } else if (Character.isLetter(word.charAt(0))) {
                   for (int k = 1; k < word.length(); k++) {
                       if (!Character.isLetter(word.charAt(k)) && !Character.isDigit(word.charAt(k))) {
                           ret.add(new Token(word.substring(0, k), TokenType.IDENTIFIER, lineNums.get(i)));
                           word = word.substring(k);
                           next = false;
                           break;
                       }
                   }
                   if (next) {
                       ret.add(new Token(word, TokenType.IDENTIFIER, lineNums.get(i)));
                   }
               }
           }
        }
        return ret;
    }

    public static List<Token> notifyInvalidToken(String word, int lineNumber, int charNumber) {
        System.out.println("Invalid token: \"" + word + "\" at " + lineNumber + ":" + charNumber);
        return new LinkedList<Token>();
    }

    enum TokenType {
        KEYWORD, OPEN_BRACE, CLOSE_BRACE, OPEN_PAREN, CLOSE_PAREN, OPEN_BRACKET, CLOSE_BRACKET,
        SEMICOLON, IDENTIFIER, INT_LITERAL, FLOAT_LITERAL, STRING_LITERAL, CHAR_LITERAL
    }

    public static class Token {

        public String value;
        public TokenType type;
        public int lineNumber;

        public Token(String value, TokenType type, int lineNumber) {
            this.value = value;
            this.type = type;
            this.lineNumber = lineNumber;
        }
    }
}

