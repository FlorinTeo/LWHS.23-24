package Threading.two;

import java.util.Timer;

public class Program {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello to MyFirstTimer!");
        ReaderThread thread = new ReaderThread();
        thread.start();

        Timer timer = new Timer();
        timer.schedule(new PromptTimer(thread), 4000, 2000);

        thread.join();
        timer.cancel();
        System.out.println("Goodbye!");
    }
}
