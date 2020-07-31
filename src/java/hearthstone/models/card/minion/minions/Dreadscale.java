package hearthstone.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.EndTurnBehave;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class Dreadscale extends MinionCard implements EndTurnBehave {
    public Dreadscale(){ }

    public Dreadscale(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect,
                      boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void endTurnBehave() {
        for(Card card: DataTransform.getInstance().getLand(DataTransform.getInstance().getEnemyId(getPlayerId()))){
            MinionCard minionCard = (MinionCard)card;
            try {
                Mapper.getInstance().damage(1, minionCard, false);
            } catch (HearthStoneException ignore) { }
        }
        Mapper.getInstance().updateBoard();
    }
}
