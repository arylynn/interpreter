import java.util.List;
import java.util.Scanner;

public class Interpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Enter your code (press Enter twice to finish, or type 'exit' to quit):");
            
            StringBuilder inputBuilder = new StringBuilder();
            String line;
            boolean firstLine = true;
            
            while (!(line = scanner.nextLine()).isEmpty() || firstLine) {
                if (line.equals("exit")) {
                    System.out.println("Exiting");
                    scanner.close();
                    return;
                }
                
                inputBuilder.append(line).append("\n");
                firstLine = false;
            }
            
            String input = inputBuilder.toString().trim();
            
            System.out.println("Input:");
            System.out.println(input);
            System.out.println("Output:");
            
            try {
                Tokenizer tokenizer = new Tokenizer();
                List<Tokenizer.Token> tokens = tokenizer.tokenize(input);
                
                Parser parser = new Parser(tokens);
                parser.parse();
                
                SymbolTable symbolTable = parser.getSymbolTable();
                
                boolean variablesPrinted = false;
                for (String varName : symbolTable.getVariableNames()) {
                    int value = symbolTable.resolve(varName);
                    System.out.println(varName + " = " + value);
                    variablesPrinted = true;
                }
                
                if (!variablesPrinted) {
                    System.out.println("Error");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error");
            } catch (RuntimeException e) {
                System.out.println("Error");
            }
            
            System.out.println();
        }
    }
}