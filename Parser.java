import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final Tokenizer tokenizer;
    private Tokenizer.Token currentToken;
    private final Map<String, Integer> variables;

    public Parser(String input) throws IOException, Tokenizer.TokenizerException {
        this.tokenizer = new Tokenizer(input);
        this.variables = new HashMap<>();
        nextToken();
    }

    private void nextToken() throws IOException, Tokenizer.TokenizerException {
        currentToken = tokenizer.nextToken();
    }

    public Map<String, Integer> parse() throws IOException, Tokenizer.TokenizerException, ParseException {
        while (currentToken.type != Tokenizer.EOF) {
            parseAssignment();
        }
        return variables;
    }

    private void parseAssignment() throws IOException, Tokenizer.TokenizerException, ParseException {
        if (currentToken.type != Tokenizer.ID) {
            throw new ParseException("Expected identifier");
        }
        String varName = currentToken.value;
        nextToken();

        if (currentToken.type != Tokenizer.ASSIGN) {
            throw new ParseException("Expected '='");
        }
        nextToken();

        int value = parseExpression();

        if (currentToken.type != Tokenizer.SEMICOLON) {
            throw new ParseException("Expected ';'");
        }
        nextToken();

        variables.put(varName, value);
    }

    private int parseExpression() throws IOException, Tokenizer.TokenizerException, ParseException {
        int value = parseTerm();

        while (currentToken.type == Tokenizer.PLUS || currentToken.type == Tokenizer.MINUS) {
            int op = currentToken.type;
            nextToken();
            int termValue = parseTerm();

            if (op == Tokenizer.PLUS) {
                value += termValue;
            } else {
                value -= termValue;
            }
        }

        return value;
    }

    private int parseTerm() throws IOException, Tokenizer.TokenizerException, ParseException {
        int value = parseFactor();

        while (currentToken.type == Tokenizer.MULTIPLY) {
            nextToken();
            int factorValue = parseFactor();
            value *= factorValue;
        }

        return value;
    }

    private int parseFactor() throws IOException, Tokenizer.TokenizerException, ParseException {
        if (currentToken.type == Tokenizer.LEFTP) {
            nextToken();
            int value = parseExpression();
            
            if (currentToken.type != Tokenizer.RIGHTP) {
                throw new ParseException("Expected ')'");
            }
            nextToken();
            return value;
        }

        if (currentToken.type == Tokenizer.MINUS) {
            nextToken();
            return -parseFactor();
        }

        if (currentToken.type == Tokenizer.PLUS) {
            nextToken();
            return parseFactor();
        }

        if (currentToken.type == Tokenizer.LITERAL) {
            int value = Integer.parseInt(currentToken.value);
            nextToken();
            return value;
        }

        if (currentToken.type == Tokenizer.ID) {
            String varName = currentToken.value;
            
            if (!variables.containsKey(varName)) {
                throw new ParseException("Uninitialized variable: " + varName);
            }
            
            int value = variables.get(varName);
            nextToken();
            return value;
        }

        throw new ParseException("Unexpected token");
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}