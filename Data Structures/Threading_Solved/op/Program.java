package Threading_Solved.op;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Program {
    public static int count = 0;
    public static Lock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello MyFirstThreads!");
        Thread tFirst = new Thread(new OpRunnable("inc"));
        Thread tSecond = new Thread(new OpRunnable("dec"));
        System.out.println(count);
        tFirst.start();
        tSecond.start();
        tFirst.join();
        tSecond.join();
        System.out.println(count);
        System.out.println("Goodbye!");
    }
}
