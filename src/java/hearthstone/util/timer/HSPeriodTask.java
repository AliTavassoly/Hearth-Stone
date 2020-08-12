package hearthstone.util.timer;

public interface HSPeriodTask {
    void startFunction();
    void periodFunction();
    boolean finishCondition();
}
