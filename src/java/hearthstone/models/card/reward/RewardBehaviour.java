package hearthstone.models.card.reward;

public interface RewardBehaviour {
    boolean metCondition();
    void doReward();
    void updatePercentage();
}
