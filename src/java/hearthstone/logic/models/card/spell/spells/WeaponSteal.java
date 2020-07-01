package hearthstone.logic.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public class WeaponSteal extends SpellCard {
    public WeaponSteal() { }

    public WeaponSteal(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        Mapper.getInstance().makeNewMouseWaiting(getCursorType(), this);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if(object instanceof WeaponCard){
            WeaponCard card = (WeaponCard)((WeaponCard) object).copy();
            Mapper.getInstance().addAttack(2, card);
            Mapper.getInstance().addDurability(2, card);
            Mapper.getInstance().makeAndPutDeck(getPlayerId(), card);
        }
    }
}
