package hearthstone.models.card.reward.rewards;

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
        Mapper.getPlayer(getPlayerId()).setManaSpentOnMinions(0);
    }

    @Override
    public int getPercentage() {
        //int now = DataTransform.spentManaOnMinions(getPlayerId());
        int now = Mapper.getPlayer(getPlayerId()).getManaSpentOnMinions();
        int end = 10;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return Mapper.getPlayer(getPlayerId()).getManaSpentOnMinions() >= 10
                /*DataTransform.spentManaOnMinions(getPlayerId()) >= 10*/;
    }

    @Override
    public void doReward() {
        //Mapper.summonMinionFromCurrentDeck(getPlayerId());
        Mapper.getPlayer(getPlayerId()).getFactory().summonMinionFromCurrentDeck();

        //Mapper.restartSpentManaOnMinions(getPlayerId());
        Mapper.getPlayer(getPlayerId()).setManaSpentOnMinions(0);

        log();

        Mapper.updateBoard();
    }
}
