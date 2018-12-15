import java.io.File;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

public class Lexer {

    private String[] keywords;

    // Requires keywords to be sorted alphabetically
    public Lexer(String[] keywords) {
        this.keywords = keywords;
    }

    // Return empty list on error
    public List<Token> lex(String fileName) {
        // TODO: Maybe add char # to token using quoteOffset 
        // TODO: Maybe hash keywords instead of binary search? - probably not neccesary due to problem size
        // TODO: BUG: Semicolons parsed to incorrect line sometimes
        ArrayList<String> code = new ArrayList<>();
		ArrayList<Integer> lineNums = new ArrayList<>(); // lineNums[significant line #] = original line #
		if (!checkBalance(getScanner(fileName), code, lineNums)) {
			return new LinkedList<Token>();
		}
		ArrayList<Token> ret = new ArrayList<>(); // return value
		String word = "";
		boolean next = true; // true when lexer is ready for next word in line split on " "
		for (int i = 0; i < code.size(); i++) {
			int quoteOffset = 0;
			String line = code.get(i);
			String[] words = line.split(" ");
			for (int j = 0; j < words.length; j++) {
				if (next) {
					word = words[j];
				} else {
					j--;
				}
				next = true;
				if (word.trim().length() == 0) {
					continue;
				}
				if (Arrays.binarySearch(keywords, word) > 0) { // keyword
					ret.add(new Token(word, TokenType.KEYWORD, lineNums.get(i)));
				} else if (Character.isDigit(word.charAt(0))) { // number literal or error
					boolean decimal = false;
					for (int k = 1; k < word.length(); k++) {
						if (Character.isDigit(word.charAt(k))) {
							continue;
						}
						if (word.charAt(k) == '.') {
							if (decimal) {
								return notifyInvalidToken(word, lineNums.get(i), line.indexOf(word));
							}
							decimal = true;
						} else if (Character.isLetter(word.charAt(k))) {
							return notifyInvalidToken(word, lineNums.get(i), line.indexOf(word));
						} else { // handles cases with no spaces, ex. word = "123+456"
							ret.add(new Token(word.substring(0, k),
									decimal ? TokenType.FLOAT_LITERAL : TokenType.INT_LITERAL, lineNums.get(i)));
							word = word.substring(k);
							next = false;
							break;
						}
					}
					if (next) {
						ret.add(new Token(word, decimal ? TokenType.FLOAT_LITERAL : TokenType.INT_LITERAL,
								lineNums.get(i)));
					}
				} else if (Character.isLetter(word.charAt(0))) { // identifier
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
				} else if (word.charAt(0) == '"') { // string literal - already handled errors in checkBalance
					for (int k = 1; k < word.length(); k++) {
						if (word.charAt(k) == '"' && checkEndString(word, k - 1)) {
							ret.add(new Token(word.substring(0, k + 1), TokenType.STRING_LITERAL, lineNums.get(i)));
							word = word.substring(k + 1);
							next = false;
							break;
						}
					}
					if (next) { // check original line to correctly handle multiple spaces
						int indexHere = quoteOffset + line.substring(quoteOffset).indexOf(word);
						quoteOffset = indexHere + word.length();
						do {
							quoteOffset += line.substring(quoteOffset).indexOf('"') + 1;
						} while (!checkEndString(line, quoteOffset - 2));
						ret.add(new Token(line.substring(indexHere, quoteOffset), TokenType.STRING_LITERAL,
								lineNums.get(i)));
						words = line.substring(quoteOffset).split(" ");
						j = -1;
					}
				} else if (word.charAt(0) == '\'') { // char literal
                    for (int k = 1; k < word.length(); k++) {
						if (word.charAt(k) == '\'' && (word.charAt(k - 1) != '\\' || word.charAt(k - 2) == '\\')) {
							ret.add(new Token(word.substring(0, k + 1), TokenType.CHAR_LITERAL, lineNums.get(i)));
							word = word.substring(k + 1);
							next = false;
							break;
						}
					}
					if (next) { // assume ' '
						ret.add(new Token(word + " \'", TokenType.CHAR_LITERAL, lineNums.get(i)));
                        for (; words[j + 1].length() == 0; j++);
                        words[j + 1] = words[j + 1].substring(1);
					}
				} else { // symbol
					ret.add(new Token("" + word.charAt(0), TokenType.SYMBOL, lineNums.get(i)));
					next = false;
					word = word.substring(1);
				}
			}
		}
        for (int i = 0; i < ret.size(); i++) {
            if (ret.get(i).value.length() == 0 || ret.get(i).value.charAt(0) == 9) { // handle tab edge case
                ret.remove(i);
                i--;
            }
        }
        return ret;
	}

    // Return scanner with file name or error code: null
    private Scanner getScanner(String filename) {
		try {
			return new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.println("File: " + filename + " not found.");
		}
		return null;
	}


    // Return error value from lex and notifys user specific location of error
	private List<Token> notifyInvalidToken(String word, int lineNumber, int charNumber) {
		System.out.println("Invalid token: \"" + word + "\" at " + lineNumber + ":" + charNumber);
		return new LinkedList<Token>();
	}

    // Return whether a given quotation mark should end a string literal.
    // Example : \\" should end string, but \\\" should not
    private boolean checkEndString(String word, int index) {
        boolean evenSlashCount = true;
        for (int offset = 0; word.charAt(index + offset) == '\\'; offset--) {
            evenSlashCount = !evenSlashCount;
        }
        return evenSlashCount;
    }

    // Checks for balanced parentheses and quotes: [], {}, () ""
    // Parses out comments
    // Stores parsed lines in List<String> code
    private boolean checkBalance(Scanner s, List<String> code, List<Integer> lineNums) {
        if (s == null) {
			return false;
		}
		boolean quote = false; // true when current code is part of a quotation
		boolean comment = false; // true when current code is part of a multi-line comment
		Stack<Character> parens = new Stack<>(); // stores {, [, (
		Stack<String> locations = new Stack<>(); // stores locations of {,[,( 
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
                if (quote && c == '\\' && charNumber < line.length() - 1) { // parse any \. as a valid char for now
                    parsedLine.append("" + c + line.charAt(++charNumber));
                    continue;
                }
				if (prev == '\'' && line.charAt(charNumber + 1) == '\'') { // character is a char literal
				} else if (c == '"') { // flip quote
					quote = !quote;
				} else if (quote) { // do nothing if character is in the middle of a current quote
				} else if (c == '/' && prev == '/') { // ignore rest of line for beginning of comment
					parsedLine.deleteCharAt(parsedLine.length() - 1);
					break;
				} else if (c == '*' && prev == '/') { // start of /* comment
					parsedLine.deleteCharAt(parsedLine.length() - 1);
				comment = true;
					continue;
				} else if (c == '{' || c == '[' || c == '(') { // open parens
					parens.push(c);
					locations.push(lineNumber + ":" + charNumber);
				} else if (c == '}' || c == ']' || c == ')') { // closed parens
					if (parens.size() == 0) {
						System.out.println("Error: Found unexpected '" + c + "' at " + lineNumber + ":" + charNumber);
						return false;
					}
					char paren = parens.pop();
					if (Math.abs(c - paren) > 2) { // unmatched case
						System.out.println("Error: '" + paren + "' opened at " + locations.pop() + " closed with '" + c
								+ "' at " + lineNumber + ":" + charNumber);
						return false;
					}
					locations.pop();
				}
				parsedLine.append(c);
			}
			if (quote) { // quotations must end on the same line
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
}

