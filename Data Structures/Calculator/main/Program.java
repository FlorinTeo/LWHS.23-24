package Calculator.main;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        System.out.println("Welcome to Smart Calculator!");
        
        // instantiate the calculator engine
        NumCalc numCalc = new NumCalc();

        // command loop for entering an expression and getting its
        // evaluated result printed out, along with the evaluation trace.
        Scanner input = new Scanner(System.in);
        do {
            System.out.printf("Expression?> ");
            String line = input.nextLine().trim();
            
            // skip over empty lines.
            if (line.isEmpty()) {
                continue;
            }
            
            // exit if the user enters either "quit" or "exit".
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                break;
            }
            
            // at this point the input is an expression that needs evaluation
            // but there may be various errors, so catch any exceptions.
            try {
                String result = numCalc.evaluate(line);
                System.out.printf("%s\n", result);
                
                // print the evaluation trace, as recorded in the calculator engine.
                System.out.printf("Evaluation trace: -------\n");
                System.out.println(numCalc);
            } catch (Exception e) {
                // in case of any exception, print the exception message
                // and the full stack trace.
                // System.out.println();
                System.out.printf("##Error: %s\n", e.getMessage());
                // System.out.printf("##Stack: ----------------\n");
                // e.printStackTrace();
                System.out.println();
            }
        } while (true);

        System.out.println("Goodbye!");
        input.close();
    }
}
