package hearthstone.data.bean;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck {
    private Hero hero;
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<Integer, Integer> numberOfCard = new HashMap<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Map<Integer, Integer> getNumberOfCard() {
        return numberOfCard;
    }

    public int currentNumberOfCards() {
        int sum = 0;
        for (Card card : cards) {
            sum += numberOfCard.get(card.getId());
        }
        return sum;
    }

    public boolean canAdd(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (currentNumberOfCards() + cnt > HearthStone.maxDeckSize) {
            return false;
        }
        if (card.getHeroType() != hero.getType()) {
            return false;
        }
        if (numberOfCard.get(card.getId()) + cnt > HearthStone.maxDeckSize) {
            return false;
        }
        return true;
    }
    public void add(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public boolean canRemove(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if(numberOfCard.get(card.getId()) - cnt >= 0){
            return true;
        }
        else{
            return false;
        }
    }
    public void remove(Card card, int cnt){
        numberOfCard.putIfAbsent(card.getId(), 0);
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if(numberOfCard.get(card.getId()) == 0){
            cards.remove(card);
        }
    }
}
