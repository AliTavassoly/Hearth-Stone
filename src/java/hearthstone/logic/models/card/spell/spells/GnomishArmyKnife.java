package hearthstone.logic.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public class GnomishArmyKnife  extends SpellCard {
    public GnomishArmyKnife() { }

    public GnomishArmyKnife(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
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
        if(object instanceof MinionCard){
            MinionCard minionCard = (MinionCard)object;
            Mapper.getInstance().setDivineShield(true, minionCard);
            Mapper.getInstance().setTaunt(true, minionCard);
            Mapper.getInstance().setCharge(true, minionCard);
        }
    }
}
