package hearthstone.models.card.reward.rewards;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class StrengthInNumbers extends RewardCard implements Battlecry {
    public StrengthInNumbers(){ }

    public StrengthInNumbers(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void battlecry() {
        //Mapper.restartSpentManaOnMinions(getPlayerId());
        DataTransform.getPlayer(getPlayerId()).setManaSpentOnMinions(0);
    }

    @Override
    public int getPercentage() {
        int now = DataTransform.spentManaOnMinions(getPlayerId());
        int end = 10;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return DataTransform.spentManaOnMinions(getPlayerId()) >= 10;
    }

    @Override
    public void doReward() {
        //Mapper.summonMinionFromCurrentDeck(getPlayerId());
        DataTransform.getPlayer(getPlayerId()).getFactory().summonMinionFromCurrentDeck();

        //Mapper.restartSpentManaOnMinions(getPlayerId());
        DataTransform.getPlayer(getPlayerId()).setManaSpentOnMinions(0);

        log();

        Mapper.updateBoard();
    }
}
