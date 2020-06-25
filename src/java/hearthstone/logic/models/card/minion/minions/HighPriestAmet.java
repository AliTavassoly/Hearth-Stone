package hearthstone.logic.models.card.minion.minions;

import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.WaitDrawingCard;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public class HighPriestAmet extends MinionCard implements WaitDrawingCard {
    public HighPriestAmet(){ }

    public HighPriestAmet(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush, minionType);

        hasWaitingForDraw = true;
    }

    @Override
    public boolean waitDrawingCard(Card card) throws HearthStoneException {
        if (card.getCardType() != CardType.MINIONCARD)
            return false;
        MinionCard minionCard = (MinionCard) card;
        Mapper.getInstance().setHealth(this.getHealth(), minionCard);
        return true;
    }
}
