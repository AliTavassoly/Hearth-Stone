package hearthstone.models.card.minion.minions;

import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class WrathspikeBrute extends MinionCard implements IsAttacked{
    public WrathspikeBrute(){ }

    public WrathspikeBrute(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                           boolean isDeathRattle, boolean isTriggeredEffect,
                           boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                           boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }


    @Override
    public void isAttacked() {
        Hero hero = HSServer.getInstance().getPlayer(enemyPlayerId).getHero();

        try {
            //Mapper.damage(1, hero);
            hero.gotDamage(1);
            // Mapper.updateBoard();
            HSServer.getInstance().updateGame(playerId);
        } catch (HearthStoneException ignore) { }

        for(Card card: HSServer.getInstance().getPlayer(enemyPlayerId).getLand()){
            try {
                //Mapper.damage(1, (MinionCard) card, false);
                ((MinionCard) card).gotDamage(1);
            } catch (HearthStoneException ignore) { }
        }
        // Mapper.updateBoard();
        HSServer.getInstance().updateGame(playerId);
    }
}
