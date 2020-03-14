package hearthstone.data.bean;

import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Collection {
    private Hero hero;
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<Integer, Integer> numberOfCard = new HashMap<>();
    private int collectionMaxSize;
    private int maxNumberOfCard;

    Collection() {
        maxNumberOfCard = 2;
        collectionMaxSize = 50;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Map<Integer, Integer> getNumberOfCard() {
        return numberOfCard;
    }

    public void setCollectionMaxSize(int collectionMaxSize) {
        this.collectionMaxSize = collectionMaxSize;
    }

    public int getCollectionMaxSize() {
        return collectionMaxSize;
    }

    public void setMaxNumberOfCard(int maxNumberOfCard) {
        this.maxNumberOfCard = maxNumberOfCard;
    }

    public int getMaxNumberOfCard() {
        return maxNumberOfCard;
    }

    public int getNumberOfAllCards(){
        int sum = 0;
        for(Card card : cards){
            sum += numberOfCard.get(card.getId());
        }
        return sum;
    }

    public boolean canAdd(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (card.getHeroType() == HeroType.ALL || card.getHeroType() != hero.getType()) {
            return false;
        }
        if (numberOfCard.get(card.getId()) + cnt > maxNumberOfCard || getNumberOfAllCards() + cnt > collectionMaxSize) {
            return false;
        }
        return true;
    }

    public void add(Card card, int cnt) throws Exception {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (card.getHeroType() != HeroType.ALL && card.getHeroType() != hero.getType()) {
            throw new HearthStoneException("Hero does not match !");
        }
        if(numberOfCard.get(card.getId()) + cnt > maxNumberOfCard){
            throw new HearthStoneException("Can not have this number of this card !");
        }
        if(getNumberOfAllCards() + cnt > collectionMaxSize){
            throw new HearthStoneException("Not enough space !");
        }
        if (numberOfCard.get(card.getId()) == 0) {
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public boolean canRemove(Card card, int cnt){
        return numberOfCard.get(card.getId()) - cnt >= 0;
    }

    public void remove(Card card, int cnt) throws Exception {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) - cnt < 0) {
            throw new HearthStoneException("It does not exist this number from this card !");
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.remove(card);
        }
    }
}
