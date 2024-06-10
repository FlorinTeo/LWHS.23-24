package Threading_Solved.op;

public class OpRunnable implements Runnable {
    private String _message;

    public OpRunnable(String message) {
        _message = message;
    }

    @Override
    public void run() {
        try {
            for(int i = 0; i < 100; i++) {
                //Program.lock.lock();
                int register = Program.count;
                switch(_message.toLowerCase()) {
                    case "inc":
                        register++;
                        break;
                    case "dec":
                        register--;
                        break;
                }
                Thread.sleep(1);
                Program.count = register;
                //Program.lock.unlock();
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
