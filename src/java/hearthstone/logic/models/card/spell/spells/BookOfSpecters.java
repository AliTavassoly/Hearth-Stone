package hearthstone.logic.models.card.spell.spells;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class BookOfSpecters  extends SpellCard {
    public BookOfSpecters() { }

    public BookOfSpecters(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> topCards = DataTransform.getInstance().getTopCards(getPlayerId(), 3);
        for(Card card: topCards){
            if(card.getCardType() != CardType.SPELL){
                try {
                    Mapper.getInstance().drawCard(getPlayerId(), card);
                } catch (HearthStoneException ignore){}
            }
        }
    }
}
