package Threading.print;

public class PrintRunnable implements Runnable {
    private String _message;

    public PrintRunnable(String message) {
        _message = message;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(_message);
        }
    }
}
