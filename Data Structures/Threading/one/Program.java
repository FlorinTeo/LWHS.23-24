package Threading.one;
import java.util.Scanner;
import java.util.Timer;

public class Program {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello to MyFirstTimer!");
        Scanner input = new Scanner(System.in);
        System.out.print("Enter text> ");

        Timer timer = new Timer();
        timer.schedule(new PromptTimer(), 4000, 2000);
        String s = input.nextLine();
        timer.cancel();

        System.out.println(s);
        input.close();        
        System.out.println("Goodbye!");
    }
}
