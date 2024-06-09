package Threading.two;
import java.util.Scanner;

public class TimeoutExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(System.getProperty("line.separator"));

        System.out.print("Enter something: ");
        String userInput = readWithTimeout(scanner, 6000); // 6 seconds timeout

        if (userInput.isEmpty()) {
            System.out.println("Timed out. Using default value.");
            // Set your default value here
        } else {
            System.out.println("User input: " + userInput);
        }
    }

    private static String readWithTimeout(Scanner scanner, long timeoutMillis) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (scanner.hasNext()) {
                return scanner.next();
            }
        }
        return ""; // Timed out
    }
}
