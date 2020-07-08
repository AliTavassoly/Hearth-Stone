package hearthstone.util.timer;

public class HSTimerTask extends Thread {
    private IHSBigTask task;
    private long period, length;
    private long warningTime;
    private final long startTime;
    private boolean shouldStop;

    public HSTimerTask(long period, long length, long warningTime, IHSBigTask task) {
        this.period = period;
        this.length = length;
        this.task = task;
        this.warningTime = warningTime;

        startTime = System.currentTimeMillis();
    }

    public HSTimerTask(long period, long length, IHSBigTask task) {
        this.period = period;
        this.length = length;
        this.task = task;

        startTime = System.currentTimeMillis();
    }

    public HSTimerTask(long period, IHSBigTask task) {
        this.period = period;
        this.task = task;

        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() throws NullPointerException {
        boolean flag = false;

        task.startFunction();
        while (!shouldStop && (length == 0 || System.currentTimeMillis() - startTime < length) && !task.finishCondition()) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            task.periodFunction();

            if (!flag && System.currentTimeMillis() - startTime + warningTime >= length) {
                task.warningFunction();
                flag = true;
            }
        }
        if (!shouldStop)
            task.finishedFunction();
        task.closeFunction();
    }

    public void myStop() {
        shouldStop = true;
    }

    public void start(){
        super.start();
    }
}