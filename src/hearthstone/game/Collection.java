package hearthstone.game;

import hearthstone.game.cards.Card;
import hearthstone.game.heroes.Hero;
import hearthstone.game.heroes.HeroType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Collection {
    private Hero hero;
    public ArrayList<Card> cards = new ArrayList<>();
    public Map<Integer, Integer> numberOfCard = new HashMap<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Map<Integer, Integer> getNumberOfCard() { return numberOfCard; }

    public boolean canAdd(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (card.getHeroType() == HeroType.ALL || card.getHeroType() != hero.getType()) {
            //System.err.println("This card can not use for this hero !");
            return false;
        }
        if (numberOfCard.get(card.getId()) + cnt > 2) {
            //System.err.println("Can not have from this more than 2 !");
            return false;
        }
        return true;
    }

    public void add(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if(numberOfCard.get(card.getId()) == 0){
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public boolean canRemove(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) - cnt < 0) {
            //System.err.println("There is no card of this type in your deck !");
            return false;
        }
        return true;
    }

    public void remove(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if(numberOfCard.get(card.getId()) == 0){
            cards.remove(card);
        }
    }
}
