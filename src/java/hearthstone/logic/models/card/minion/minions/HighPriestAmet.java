package hearthstone.logic.models.card.minion.minions;

import hearthstone.Mapper;
import hearthstone.logic.behaviours.WaitSummonCard;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;

public class HighPriestAmet extends MinionCard implements WaitSummonCard {
    public HighPriestAmet(){ }

    public HighPriestAmet(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect,
                          boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public boolean waitSummonCard(Card card) {
        if (card.getCardType() != CardType.MINIONCARD)
            return false;
        MinionCard minionCard = (MinionCard) card;
        Mapper.getInstance().setHealth(this.getHealth(), minionCard);
        return true;
    }
}
