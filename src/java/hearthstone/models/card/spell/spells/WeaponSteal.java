package hearthstone.models.card.spell.spells;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.CursorType;

import javax.persistence.Entity;

@Entity
public class WeaponSteal extends SpellCard {
    public WeaponSteal() { }

    public WeaponSteal(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
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
        if(object instanceof WeaponCard){
            WeaponCard card = (WeaponCard)((WeaponCard) object).copy();
            //Mapper.addAttack(2, card);
            card.setAttack(card.getAttack() + 2);
            //updateBoard();

            //Mapper.addDurability(2, card);
            card.setDurability(card.getDurability() + 2);
            // Mapper.updateBoard();
            HSServer.getInstance().updateGameRequest(playerId);

            //Mapper.makeAndPutDeck(getPlayerId(), card);
            HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndPutDeck(card);
        }
    }
}
