package main;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        System.out.println("Welcome to Smart Calculator!");
        
        // instantiate the numerical calculator
        NumCalc numCalc = new NumCalc();

        // command loop for entering an expression and getting its
        // evaluated result, along with its evaluation trace.
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
                
                // print the evaluation trace of this last evaluation.
                System.out.printf("Evaluation trace: -------\n");
                System.out.println(numCalc);
            } catch (Exception e) {
                // in case of any exception, print the exception message
                System.out.printf("##Error: %s\n", e.getMessage());
                System.out.println();
            }
        } while (true);

        System.out.println("Goodbye!");
        input.close();
    }
}
