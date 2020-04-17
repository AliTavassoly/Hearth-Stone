package hearthstone.logic.models;

import hearthstone.logic.models.cards.Card;
import hearthstone.logic.models.cards.CardType;
import hearthstone.logic.models.cards.MinionCard;

import java.util.ArrayList;
import java.util.HashMap;

public class Deck {
    private String name;
    private int totalGame;
    private int winGame;
    private HashMap<Integer, Integer> cardGame;
    private ArrayList<Card> cards = new ArrayList<>();

    public Deck(){ }

    public Deck(String name){
        cardGame = new HashMap<>();
        this.name = name;
    }

    public HashMap<Integer, Integer> getCardGame() {
        return cardGame;
    }

    public void setCardGame(HashMap<Integer, Integer> cardGame) {
        this.cardGame = cardGame;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public int getTotalGame() {
        return totalGame;
    }

    public void setTotalGame(int totalGame) {
        this.totalGame = totalGame;
    }

    public int getWinGame() {
        return winGame;
    }

    public void setWinGame(int winGame) {
        this.winGame = winGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWinTotal(){
        if (totalGame == 0)
            return 0;
        return winGame * 100 / totalGame;
    }

    public int getManaAv(){
        if (cards.size() == 0)
            return 0;
        int totalMana = 0;
        for(Card card : cards){
            totalMana += card.getManaCost();
        }
        return totalMana / cards.size();
    }

    private Card biggerCard(Card card1, Card card2){
        if(cardGame.get(card1.getId()) > cardGame.get(card2.getId())){
            return card1;
        } else if(cardGame.get(card1.getId()) < cardGame.get(card2.getId())){
            return card2;
        } else{
            // ENUM
            if(card1.getManaCost() > card2.getManaCost()){
                return card1;
            } else if(card1.getManaCost() < card2.getManaCost()){
                return card2;
            } else {
                if(card1 instanceof MinionCard)
                    return card1;
                return card2;
            }
        }
    }

    public Card getBestCard(){
        if(cards.size() == 0)
            return null;
        Card mxCard = cards.get(0);
        for(int i = 1; i < cards.size(); i++){
             Card card = cards.get(i);
             mxCard = biggerCard(mxCard, card);
        }
        return mxCard;
    }
}
