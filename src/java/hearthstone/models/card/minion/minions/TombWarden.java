package hearthstone.models.card.minion.minions;

import hearthstone.models.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class TombWarden extends MinionCard implements Battlecry {
    public TombWarden(){ }

    public TombWarden(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect,
                      boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void battlecry() {
        //Mapper.makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName(this.getName()));
        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName(this.getName()));

        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);
    }
}
