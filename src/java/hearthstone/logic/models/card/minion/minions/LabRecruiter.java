package hearthstone.logic.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.interfaces.Battlecry;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;

public class LabRecruiter extends MinionCard implements Battlecry {
    public LabRecruiter(){ }

    public LabRecruiter(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                        boolean isDeathRattle, boolean isTriggeredEffect,
                        boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                        boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void battlecry() {
        MinionCard minionCard = (MinionCard) DataTransform.getInstance().getRandomCardFromOriginalDeck(
                getPlayerId(),
                CardType.MINIONCARD);

        if(minionCard == null)
            return;

        Mapper.getInstance().makeAndPutDeck(getPlayerId(), minionCard.copy());
        Mapper.getInstance().makeAndPutDeck(getPlayerId(), minionCard.copy());
        Mapper.getInstance().makeAndPutDeck(getPlayerId(), minionCard.copy());
    }
}