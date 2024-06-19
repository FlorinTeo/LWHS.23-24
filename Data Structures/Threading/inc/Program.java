package Threading.inc;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello MyFirstThreads!");
        SharedInt sharedInt = new SharedInt();
        Thread t1 = new Thread(new IncRunnable(sharedInt));
        Thread t2 = new Thread(new IncRunnable(sharedInt));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(sharedInt.getNum());
        System.out.println("Goodbye!");
    }
}
