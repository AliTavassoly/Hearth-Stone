package hearthstone.logic.models.card.reward;

public interface RewardBehaviour {
    boolean metCondition();
    void doReward();
    int getPercentage();
}
