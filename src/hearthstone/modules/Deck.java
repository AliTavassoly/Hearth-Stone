package hearthstone.modules;

import hearthstone.HearthStone;
import hearthstone.modules.cards.Card;
import hearthstone.modules.heroes.Hero;
import hearthstone.modules.heroes.HeroType;
import hearthstone.util.HearthStoneException;

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
        if (card.getHeroType()!= HeroType.ALL && card.getHeroType()!= hero.getType()) {
            return false;
        }
        if (numberOfCard.get(card.getId()) + cnt > HearthStone.maxNumberOfCard) {
            return false;
        }
        return true;
    }

    public void add(Card card, int cnt) throws Exception {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (currentNumberOfCards() + cnt > HearthStone.maxDeckSize) {
            throw new HearthStoneException("Not enough space in your deck!");
        }
        if (card.getHeroType()!= HeroType.ALL && card.getHeroType()!= hero.getType()) {
            throw new HearthStoneException("Hero does not match!");
        }
        if (numberOfCard.get(card.getId()) + cnt > HearthStone.maxNumberOfCard) {
            throw new HearthStoneException("You can not have " + cnt + " number of this card!");
        }
        if (numberOfCard.get(card.getId()) == 0) {
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public boolean canRemove(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        return numberOfCard.get(card.getId()) - cnt >= 0;
    }

    public void remove(Card card, int cnt) throws Exception{
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) - cnt >= 0) {
            throw new HearthStoneException("There is not " + cnt + " number of this card in your deck!");
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.remove(card);
        }
    }
}
