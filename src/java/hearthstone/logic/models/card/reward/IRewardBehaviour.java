package hearthstone.logic.models.card.reward;

public interface IRewardBehaviour {
    boolean metCondition();
    void doReward();
    int getPercentage();
}
