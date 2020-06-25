package hearthstone.logic.models.card.reward.rewards;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.Battlecry;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.hero.HeroType;

public class LearnDraconic extends RewardCard implements Battlecry {
    public LearnDraconic(){ }

    public LearnDraconic(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void battlecry() {
        DataTransform.getInstance().restartSpentManaOnSpells(getPlayer().getPlayerId());
    }

    @Override
    public int getPercentage() {
        int now = DataTransform.getInstance().spentManaOnSpells(getPlayer().getPlayerId());
        int end = 8;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return getPlayer().getManaSpentOnSpells() >= 8;
    }

    @Override
    public void doReward() {
        getPlayer().makeAndSummonMinion(HearthStone.getCardByName("Faerie Dragon"));
        DataTransform.getInstance().restartSpentManaOnSpells(getPlayer().getPlayerId());
    }
}
