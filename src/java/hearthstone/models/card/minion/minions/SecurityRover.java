package hearthstone.models.card.minion.minions;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class SecurityRover extends MinionCard implements IsAttacked {
    public SecurityRover(){ }

    public SecurityRover(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                        boolean isDeathRattle, boolean isTriggeredEffect,
                         boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                        boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void isAttacked() {
        //Mapper.makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("The Hulk"));
        Mapper.getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(HSServer.getCardByName("The Hulk"));

        Mapper.updateBoard();
    }
}
