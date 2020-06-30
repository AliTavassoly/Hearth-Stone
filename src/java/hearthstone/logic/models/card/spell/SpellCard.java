package hearthstone.logic.models.card.spell;

import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public abstract class SpellCard extends Card implements SpellBehaviour{
    public SpellCard() { }

    public SpellCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.NORMAL;
    }
}
