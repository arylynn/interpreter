import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Tokenizer {
    public static final int 
        EOF = 256,
        ID = 257,
        LITERAL = 258,
        ASSIGN = 259,
        SEMICOLON = 260,
        PLUS = 261,
        MINUS = 262,
        MULTIPLY = 263,
        LEFTP = 264,
        RIGHTP = 265;

    public static class Token {
        public final int type;
        public final String value;

        public Token(int type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    private final PushbackReader reader;
    private int currentChar;

    public Tokenizer(String input) throws IOException {
        this.reader = new PushbackReader(new StringReader(input));
        nextChar();
    }

    private void nextChar() throws IOException {
        currentChar = reader.read();
    }

    private boolean isLetter(int c) {
        return (c >= 'a' && c <= 'z') || 
               (c >= 'A' && c <= 'Z') || 
               c == '_';
    }

    private boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    private void skipWhitespace() throws IOException {
        while (Character.isWhitespace(currentChar)) {
            nextChar();
        }
    }

    public Token nextToken() throws IOException, TokenizerException {
        skipWhitespace();

        if (currentChar == -1) {
            return new Token(EOF, "");
        }

        if (isLetter(currentChar)) {
            return parseIdentifier();
        }

        if (isDigit(currentChar)) {
            return parseLiteral();
        }

        switch (currentChar) {
            case '=' -> {
                nextChar();
                return new Token(ASSIGN, "=");
            }
            case ';' -> {
                nextChar();
                return new Token(SEMICOLON, ";");
            }
            case '+' -> {
                nextChar();
                return new Token(PLUS, "+");
            }
            case '-' -> {
                nextChar();
                return new Token(MINUS, "-");
            }
            case '*' -> {
                nextChar();
                return new Token(MULTIPLY, "*");
            }
            case '(' -> {
                nextChar();
                return new Token(LEFTP, "(");
            }
            case ')' -> {
                nextChar();
                return new Token(RIGHTP, ")");
            }
            default -> throw new TokenizerException("Unexpected character: " + (char)currentChar);
        }
    }

    private Token parseIdentifier() throws IOException {
        StringBuilder sb = new StringBuilder();
        
        sb.append((char)currentChar);
        nextChar();

        while (isLetter(currentChar) || isDigit(currentChar)) {
            sb.append((char)currentChar);
            nextChar();
        }

        return new Token(ID, sb.toString());
    }

    private Token parseLiteral() throws IOException, TokenizerException {
        if (currentChar == '0') {
            nextChar();
            if (isDigit(currentChar)) {
                throw new TokenizerException("Leading zeros are not allowed");
            }
            return new Token(LITERAL, "0");
        }

        StringBuilder sb = new StringBuilder();
        sb.append((char)currentChar);
        nextChar();

        while (isDigit(currentChar)) {
            sb.append((char)currentChar);
            nextChar();
        }

        return new Token(LITERAL, sb.toString());
    }

    public static class TokenizerException extends Exception {
        public TokenizerException(String message) {
            super(message);
        }
    }
}