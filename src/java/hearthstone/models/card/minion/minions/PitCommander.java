package hearthstone.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.EndTurnBehave;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class PitCommander extends MinionCard implements EndTurnBehave {
    @Transient
    private boolean didItEndTurnBehave;

    public PitCommander(){ }

    public PitCommander(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                        boolean isDeathRattle, boolean isTriggeredEffect,
                        boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                        boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void endTurnBehave() {
        if(didItEndTurnBehave)
            return;

        //Mapper.summonMinionFromCurrentDeck(getPlayerId(), MinionType.DEMON);
        DataTransform.getPlayer(getPlayerId()).getFactory().summonMinionFromCurrentDeck(MinionType.DEMON);

        didItEndTurnBehave = true;

        Mapper.updateBoard();
    }
}
