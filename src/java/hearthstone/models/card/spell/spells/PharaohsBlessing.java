package hearthstone.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

public class PharaohsBlessing extends SpellCard {
    public PharaohsBlessing() { }

    public PharaohsBlessing(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
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
            Mapper.getInstance().addAttack(4, minionCard);
            Mapper.getInstance().addHealth(4, minionCard);
            Mapper.getInstance().setTaunt(true, minionCard);
            Mapper.getInstance().setDivineShield(true, minionCard);

            Mapper.getInstance().updateBoard();
        }
    }
}
