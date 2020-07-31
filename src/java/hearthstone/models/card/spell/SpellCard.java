package hearthstone.models.card.spell;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

import javax.persistence.Entity;

@Entity
public abstract class SpellCard extends Card implements SpellBehaviour {
    public SpellCard() { }

    public SpellCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.NORMAL;
    }
}
