package hearthstone.util.timer;

public class HSPeriodTimer extends Thread{
    private HSPeriodTask task;
    private long period;

    public HSPeriodTimer(long period, HSPeriodTask task) {
        this.period = period;
        this.task = task;
    }

    @Override
    public void run() throws NullPointerException {
        task.startFunction();
        while (!task.finishCondition()) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            task.periodFunction();
        }
    }

    public void start(){
        super.start();
    }
}
