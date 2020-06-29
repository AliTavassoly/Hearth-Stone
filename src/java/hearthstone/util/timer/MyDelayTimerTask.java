package hearthstone.util.timer;

public class MyDelayTimerTask extends Thread {
    private MyDelayTask task;
    private long delay;

    public MyDelayTimerTask(long delay, MyDelayTask task) {
        this.delay = delay;
        this.task = task;

        this.start();
    }

    @Override
    public void run() throws NullPointerException {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.delayAction();
    }
}
