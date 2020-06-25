package hearthstone.logic.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.EndTurnBehave;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.HeroType;

public class Dreadscale extends MinionCard implements EndTurnBehave {
    public Dreadscale(){ }

    public Dreadscale(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush, minionType);

        hasEndTurnBehave = true;
    }

    @Override
    public void endTurnBehave() {
        for(Card card: DataTransform.getInstance().getLand(this.getPlayer().getEnemyPlayerId())){
            MinionCard minionCard = (MinionCard)card;
            Mapper.getInstance().damage(1, minionCard);
        }
        Mapper.getInstance().updateBoard();
    }
}
