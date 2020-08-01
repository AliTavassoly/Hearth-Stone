package hearthstone.models.card.reward.rewards;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.models.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class LearnDraconic extends RewardCard implements Battlecry {
    public LearnDraconic(){ }

    public LearnDraconic(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void battlecry() {
        Mapper.getInstance().restartSpentManaOnSpells(getPlayerId());
    }

    @Override
    public int getPercentage() {
        int now = DataTransform.getInstance().spentManaOnSpells(getPlayerId());
        int end = 8;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return DataTransform.getInstance().spentManaOnSpells(getPlayerId()) >= 8;
    }

    @Override
    public void doReward() {
        Mapper.getInstance().makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Faerie Dragon"));
        Mapper.getInstance().restartSpentManaOnSpells(getPlayerId());
        log();

        Mapper.getInstance().updateBoard();
    }
}
