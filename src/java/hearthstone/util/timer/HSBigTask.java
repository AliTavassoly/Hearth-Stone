package hearthstone.util.timer;

public interface HSBigTask {
    void startFunction();

    void periodFunction();

    void warningFunction();

    void finishedFunction();

    void closeFunction();

    boolean finishCondition();
}
