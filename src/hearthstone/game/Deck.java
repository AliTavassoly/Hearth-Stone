package hearthstone.game;

import hearthstone.game.cards.Card;
import hearthstone.game.heroes.Hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck {
    private Hero hero;
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<Integer, Integer> numberOfCard = new HashMap<>();
    private int deckMaxSize;
    private int maxNumberOfCard;

    Deck() {
        maxNumberOfCard = 2;
        deckMaxSize = 30;
    }

    public int getDeckMaxSize() {
        return deckMaxSize;
    }

    public void setDeckMaxSize(int deckMaxSize) {
        this.deckMaxSize = deckMaxSize;
    }

    public int getMaxNumberOfCard() {
        return maxNumberOfCard;
    }

    public void setMaxNumberOfCard(int maxNumberOfCard) {
        this.maxNumberOfCard = maxNumberOfCard;
    }

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
        if (currentNumberOfCards() + cnt > getDeckMaxSize()) {
            return false;
        }
        if (card.getHeroType() != hero.getType()) {
            return false;
        }
        if (numberOfCard.get(card.getId()) + cnt > maxNumberOfCard) {
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
