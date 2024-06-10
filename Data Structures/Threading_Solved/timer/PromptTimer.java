package Threading_Solved.timer;
import java.util.TimerTask;

public class PromptTimer extends TimerTask {

    @Override
    public void run() {
        System.out.println("\n.. are you still there?");
    }
}
