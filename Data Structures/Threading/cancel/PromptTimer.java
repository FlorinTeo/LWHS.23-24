package Threading.cancel;
import java.util.TimerTask;

public class PromptTimer extends TimerTask {
    private Thread _thread;
    private int _count;

    public PromptTimer(Thread thread) {
        _thread = thread;
        _count = 5;
    }

    @Override
    public void run() {
        if (_count == 0) {
            System.out.println("\n.. giving up!");
            _thread.interrupt();
            this.cancel();
        } else {
            System.out.println("\n.. are you still there?");
            _count--;
        }
    }
}
