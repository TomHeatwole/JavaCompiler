public class Token {

    public String value;
    public TokenType type;
    public int lineNumber;

    public Token(String value, TokenType type, int lineNumber) {
        this.value = value;
        this.type = type;
        this.lineNumber = lineNumber;
    }
}
