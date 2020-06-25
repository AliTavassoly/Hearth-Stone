package hearthstone.logic.models.card.spell.spells;

import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellBehaviour;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;

public class PharaohsBlessing extends SpellCard implements SpellBehaviour {
    public PharaohsBlessing() { }

    public PharaohsBlessing(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}