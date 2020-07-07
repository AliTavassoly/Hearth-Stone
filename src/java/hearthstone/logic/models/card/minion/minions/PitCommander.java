package hearthstone.logic.models.card.minion.minions;

import hearthstone.Mapper;
import hearthstone.logic.interfaces.EndTurnBehave;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;

public class PitCommander extends MinionCard implements EndTurnBehave {
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
        Mapper.getInstance().summonMinionFromCurrentDeck(getPlayerId(), MinionType.DEMON);
        didItEndTurnBehave = true;

        Mapper.getInstance().updateBoard();
    }
}
