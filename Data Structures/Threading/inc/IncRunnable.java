package Threading.inc;

public class IncRunnable implements Runnable {
    private SharedInt _sharedInt;

    public IncRunnable(SharedInt sharedInt) {
        _sharedInt = sharedInt;
    }

    @Override
    public void run() {
        try {
            for(int i = 0; i < 1000; i++) {
                //synchronized(_sharedInt) {
                    int n = _sharedInt.getNum();
                    n++;
                    //Thread.sleep(0);
                    //Thread.yield();
                    _sharedInt.setNum(n);    
                //}
            }
        } catch (Exception e) {
            
        }
    }
}
