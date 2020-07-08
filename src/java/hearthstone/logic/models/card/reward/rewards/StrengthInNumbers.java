package hearthstone.logic.models.card.reward.rewards;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.Battlecry;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.hero.HeroType;

public class StrengthInNumbers extends RewardCard implements Battlecry {
    public StrengthInNumbers(){ }

    public StrengthInNumbers(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void battlecry() {
        Mapper.getInstance().restartSpentManaOnMinions(getPlayerId());
    }

    @Override
    public int getPercentage() {
        int now = DataTransform.getInstance().spentManaOnMinions(getPlayerId());
        int end = 10;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return DataTransform.getInstance().spentManaOnMinions(getPlayerId()) >= 10;
    }

    @Override
    public void doReward() {
        Mapper.getInstance().summonMinionFromCurrentDeck(getPlayerId());
        Mapper.getInstance().restartSpentManaOnMinions(getPlayerId());
        log();

        Mapper.getInstance().updateBoard();
    }
}
