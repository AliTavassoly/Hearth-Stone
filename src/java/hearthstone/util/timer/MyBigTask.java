package hearthstone.util.timer;

public interface MyBigTask {
    void startFunction();

    void periodFunction();

    void warningFunction();

    void finishedFunction();

    void closeFunction();

    boolean finishCondition();
}
