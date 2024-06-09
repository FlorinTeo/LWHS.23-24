package Threading.two;

import java.util.Scanner;

public class ReaderThread extends Thread {

    @SuppressWarnings("resource")
    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter text> ");
        try {
            while(System.in.available() == 0 && !this.isInterrupted()) {
                Thread.sleep(10);
            }
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            String s = input.nextLine();
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("[TIMED OUT]");
        }
        input.close();
    }
}
