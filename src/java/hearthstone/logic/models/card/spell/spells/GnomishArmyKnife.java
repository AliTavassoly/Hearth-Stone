package hearthstone.logic.models.card.spell.spells;

import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;

public class GnomishArmyKnife  extends SpellCard {
    public GnomishArmyKnife() { }

    public GnomishArmyKnife(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {

    }
}
