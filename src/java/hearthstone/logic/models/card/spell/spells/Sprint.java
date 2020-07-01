package hearthstone.logic.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public class Sprint extends SpellCard {
    public Sprint() {
    }

    public Sprint(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        try {
            for (int i = 0; i < 4; i++)
                Mapper.getInstance().drawCard(getPlayerId());
        } catch (HearthStoneException ignore) {
        }
    }
}
