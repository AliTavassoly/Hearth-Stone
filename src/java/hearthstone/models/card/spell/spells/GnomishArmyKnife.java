package hearthstone.models.card.spell.spells;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.CursorType;

import javax.persistence.Entity;

@Entity
public class GnomishArmyKnife  extends SpellCard {
    public GnomishArmyKnife() { }

    public GnomishArmyKnife(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        HSServer.getInstance().createMouseWaiting(playerId, getCursorType(), this);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if(object instanceof MinionCard){
            MinionCard minionCard = (MinionCard)object;
            //Mapper.setDivineShield(true, minionCard);
            minionCard.setDivineShield(true);

            //Mapper.setTaunt(true, minionCard);
            minionCard.setTaunt(true);

            //Mapper.setCharge(true, minionCard);
            minionCard.setCharge(true);

            // Mapper.updateBoard();
            HSServer.getInstance().updateGame(playerId);
        }
    }
}
