package Threading.print;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello MyFirstThreads!");
        PrintRunnable first = new PrintRunnable("ping");
        PrintRunnable second = new PrintRunnable("       pong");
        Thread tFirst = new Thread(first);
        Thread tSecond = new Thread(second);
        tFirst.start();
        tSecond.start();
        tFirst.join();
        tSecond.join();
        System.out.println("Goodbye!");
    }
}
