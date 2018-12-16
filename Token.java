public class Token {

    private String value;
    private TokenType type;
    private int lineNumber;

    public Token(String value, TokenType type, int lineNumber) {
        this.value = value;
        this.type = type;
        this.lineNumber = lineNumber;
    }

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    /* uncomment as needed
    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
    */

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean equals(Object o) {
        if (o instanceof Token) {
            Token t = (token)o;
            return (t.type == o.type && t.value == o.value);
        }
        return false;
    }

    // For debugging
    public String toString() {
        return "" + lineNumber + " " + type + " " + value;
    }
}
