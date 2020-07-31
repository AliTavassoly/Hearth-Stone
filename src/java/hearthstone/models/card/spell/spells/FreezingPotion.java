package hearthstone.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.logic.behaviours.Character;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

public class FreezingPotion  extends SpellCard {
    public FreezingPotion() { }

    public FreezingPotion(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        Mapper.getInstance().makeNewMouseWaiting(CursorType.FREEZE, this);
    }

    @Override
    public void found(Object object){
        if(object instanceof Character){
            Character character = (Character) object;
            Mapper.getInstance().addFreezes(character.getPlayerId(), 2, character);
            Mapper.getInstance().deleteCurrentMouseWaiting();
        }
    }
}
