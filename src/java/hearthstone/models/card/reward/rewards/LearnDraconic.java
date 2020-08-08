package hearthstone.models.card.reward.rewards;

import hearthstone.models.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;

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
        //Mapper.restartSpentManaOnSpells(getPlayerId());
        HSServer.getInstance().getPlayer(getPlayerId()).setManaSpentOnSpells(0);
    }

    @Override
    public int getPercentage() {
        //int now = DataTransform.spentManaOnSpells(getPlayerId());
        int now = HSServer.getInstance().getPlayer(getPlayerId()).getManaSpentOnSpells();
        int end = 8;

        return (int)(((double)now / (double)end) * 100);
    }

    @Override
    public boolean metCondition() {
        return HSServer.getInstance().getPlayer(getPlayerId()).getManaSpentOnSpells() >= 8
                /*DataTransform.spentManaOnSpells(getPlayerId()) >= 8*/ ;
    }

    @Override
    public void doReward() {
        //Mapper.makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Faerie Dragon"));
        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Faerie Dragon"));

        //Mapper.restartSpentManaOnSpells(getPlayerId());
        HSServer.getInstance().getPlayer(getPlayerId()).setManaSpentOnSpells(0);

        log();

        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);
    }
}
