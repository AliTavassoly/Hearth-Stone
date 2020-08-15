package hearthstone.models.card.reward.rewards;

import hearthstone.models.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;

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
        HSServer.getInstance().getPlayer(getPlayerId()).setManaSpentOnMinions(0);
    }

    @Override
    public void updatePercentage() {
        int now = HSServer.getInstance().getPlayer(getPlayerId()).getManaSpentOnMinions();
        int end = 10;

        percentage = (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return HSServer.getInstance().getPlayer(getPlayerId()).getManaSpentOnMinions() >= 10;
    }

    @Override
    public void doReward() {
        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().summonMinionFromCurrentDeck();

        HSServer.getInstance().getPlayer(getPlayerId()).setManaSpentOnMinions(0);

        HSServer.getInstance().updateGame(playerId);
    }
}
