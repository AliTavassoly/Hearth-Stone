package hearthstone.models.card.spell.spells;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

public class Polymorph  extends SpellCard {
    public Polymorph() { }

    public Polymorph(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        Mapper.getInstance().makeNewMouseWaiting(getCursorType(),this);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if(object instanceof MinionCard){
            MinionCard minionCard = (MinionCard)object;
            Mapper.getInstance().transformMinion(minionCard.getPlayerId(), minionCard, (MinionCard) HearthStone.getCardByName("Sheep"));

            Mapper.getInstance().updateBoard();
        }
    }
}