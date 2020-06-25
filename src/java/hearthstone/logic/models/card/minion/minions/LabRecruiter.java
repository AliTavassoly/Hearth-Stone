package hearthstone.logic.models.card.minion.minions;

import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.Battlecry;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;

public class LabRecruiter extends MinionCard implements Battlecry {
    public LabRecruiter(){ }

    public LabRecruiter(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                        boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                        boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void battlecry() {
        MinionCard minionCard = (MinionCard) getPlayer().getRandomCardFromOriginalDeck(CardType.MINIONCARD);

        if(minionCard == null)
            return;

        getPlayer().makeAndPutDeck(minionCard.copy());
        getPlayer().makeAndPutDeck(minionCard.copy());
        getPlayer().makeAndPutDeck(minionCard.copy());
    }
}