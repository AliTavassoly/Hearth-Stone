package hearthstone.util.timer;

public interface IHSBigTask {
    void startFunction();

    void periodFunction();

    void warningFunction();

    void finishedFunction();

    void closeFunction();

    boolean finishCondition();
}
