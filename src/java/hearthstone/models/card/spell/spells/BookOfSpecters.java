package hearthstone.models.card.spell.spells;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
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