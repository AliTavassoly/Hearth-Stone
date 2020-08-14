package hearthstone.models.card.spell.spells;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class BookOfSpecters  extends SpellCard {
    public BookOfSpecters() { }

    public BookOfSpecters(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> topCards = HSServer.getInstance().getPlayer(playerId).getTopCards(3);
        for(Card card: topCards){
            if(card.getCardType() != CardType.SPELL){
                try {
                    //Mapper.drawCard(getPlayerId(), card);
                    HSServer.getInstance().getPlayer(getPlayerId()).drawCard(card);
                    // Mapper.updateBoard();
                    HSServer.getInstance().updateGame(playerId);
                } catch (HearthStoneException ignore){}
            }
        }
    }
}
