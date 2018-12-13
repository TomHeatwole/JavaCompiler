import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;
import java.util.List;


public class Compile {

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Invalid number of parameters.");
            System.out.println("Usage: java Compile <name_of_java_file>");
            return;
        }
        LinkedList<String> code = new LinkedList<>();
        if (!checkBalance(getScanner(args[0]), code)) {
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
    public static boolean checkBalance(Scanner s, List<String> code) {
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
            }
        }
        if (parens.size() != 0) {
            System.out.println("Error: Unclosed '" + parens.pop() + "' opened at " + locations.pop());
            return false;
        }
        return true;
   }
}

