package hearthstone.util;

public class MyTimerTask extends Thread {
    private MyTask task;
    private final long period, length;
    private long warningTime;
    private final long startTime;
    private boolean shouldStop;

    public MyTimerTask(long period, long length, long warningTime, MyTask task) {
        this.period = period;
        this.length = length;
        this.task = task;
        this.warningTime = warningTime;

        startTime = System.currentTimeMillis();

        this.start();
    }

    public MyTimerTask(long period, long length, MyTask task) {
        this.period = period;
        this.length = length;
        this.task = task;

        startTime = System.currentTimeMillis();

        this.start();
    }

    @Override
    public void run() {
        boolean flag = false;

        task.startFunction();
        while (!shouldStop && System.currentTimeMillis() - startTime < length) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ignore) {
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
}