// import java.io.IOException;
// import java.util.Map;

// public class Interpreter {
//     public static void main(String[] args) {
//         String[] inputs = {
//             "x = 001;",
//             "x_2 = 0;",
//             "x = 0\ny = x;\nz = ---(x+y);",
//             "x = 1;\ny = 2;\nz = ---(x+y)*(x+-y);"
//         };

//         for (String input : inputs) {
//             System.out.println("Input:");
//             System.out.println(input);
//             System.out.println("Output:");
            
//             try {
//                 Parser parser = new Parser(input);
//                 Map<String, Integer> variables = parser.parse();
                
//                 // Print variables
//                 for (Map.Entry<String, Integer> entry : variables.entrySet()) {
//                     System.out.println(entry.getKey() + " = " + entry.getValue());
//                 }
//             } catch (Parser.ParseException | Tokenizer.TokenizerException | IOException e) {
//                 System.out.println("error");
//             }
//             System.out.println();
//         }
//     }
// }

import java.io.IOException;
import java.util.Map;
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
                if (!line.equals("exit")) {
                    inputBuilder.append(line).append("\n");
                } else {
                    System.out.println("Exiting the interpreter.");
                    scanner.close();
                    return;
                }
                firstLine = false;
            }
            
            String input = inputBuilder.toString().trim();
            
            System.out.println("Input:");
            System.out.println(input);
            System.out.println("Output:");
            
            try {
                Parser parser = new Parser(input);
                Map<String, Integer> variables = parser.parse();
                
                for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
            } catch (Parser.ParseException | Tokenizer.TokenizerException | IOException e) {
                System.out.println("error");
            }
            
            System.out.println();
        }
    }
}