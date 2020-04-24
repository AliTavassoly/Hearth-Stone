package hearthstone.logic.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.MinionCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.AbstractAdapter;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.HashMap;

public class Deck {
    private String name;
    private int totalGames;
    private int winGames;
    private HashMap<Integer, Integer> cardGame = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private HeroType heroType;

    public Deck() {
    }

    public Deck(String name, HeroType heroType) {
        this.name = name;
        this.heroType = heroType;
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

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getWinGames() {
        return winGames;
    }

    public void setWinGames(int winGames) {
        this.winGames = winGames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public int getTotalWin() {
        if (totalGames == 0)
            return 0;
        return winGames * 100 / totalGames;
    }

    public int getManaAv() {
        if (cards.size() == 0)
            return 0;
        int totalMana = 0;
        for (Card card : cards) {
            totalMana += card.getManaCost();
        }
        return totalMana / cards.size();
    }

    private Card biggerCard(Card card1, Card card2) {
        cardGame.putIfAbsent(card1.getId(), 0);
        cardGame.putIfAbsent(card2.getId(), 0);

        if(!cardGame.get(card1.getId()).equals(cardGame.get(card2.getId())))
            return cardGame.get(card1.getId()) > cardGame.get(card2.getId()) ? card1 : card2;
        if(card1.getRarity().getValue() != card2.getRarity().getValue())
            return card1.getRarity().getValue() > card2.getRarity().getValue() ? card1 : card2;
        if(card1.getManaCost() != card2.getManaCost())
            return card1.getManaCost() > card2.getManaCost() ? card1 : card2;
        if(card1 instanceof MinionCard || card2 instanceof MinionCard)
            return card1 instanceof MinionCard ? card1 : card2;
        return card1;
    }

    public Card getBestCard() {
        if (cards.size() == 0)
            return null;
        Card mxCard = cards.get(0).copy();
        for (int i = 1; i < cards.size(); i++) {
            Card card = cards.get(i).copy();
            mxCard = biggerCard(mxCard, card);
        }
        return mxCard;
    }

    public Deck copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Deck.class), Deck.class);
    }

    //End of getter setter !

    public int numberOfCards(Card baseCard) {
        int ans = 0;
        for (Card card : cards) {
            if (card.getId() == baseCard.getId()) {
                ans++;
            }
        }
        return ans;
    }

    public boolean canAdd(Card baseCard, int cnt) {
        if (numberOfCards(baseCard) + cnt > HearthStone.currentAccount.getCollection().numberOfCards(baseCard)) {
            return false;
        }
        if (cards.size() + cnt > HearthStone.maxCardInDeck) {
            return false;
        }
        if (numberOfCards(baseCard) + cnt > HearthStone.maxCardOfOneType) {
            return false;
        }
        if (!HearthStone.currentAccount.getUnlockedCards().contains(baseCard.getId())) {
            return false;
        }
        if (baseCard.getHeroType() != HeroType.ALL && baseCard.getHeroType() != heroType) {
            return false;
        }
        return true;
    }

    public void add(Card baseCard, int cnt) throws Exception {
        if (numberOfCards(baseCard) + cnt > HearthStone.currentAccount.getCollection().numberOfCards(baseCard)) {
            throw new HearthStoneException("You don't have " + numberOfCards(baseCard) + cnt + " numbers of this card!");
        }
        if (cards.size() + cnt > HearthStone.maxCardInDeck) {
            throw new HearthStoneException("Deck is full!");
        }
        if (numberOfCards(baseCard) + cnt > HearthStone.maxCardOfOneType) {
            throw new HearthStoneException("You can not have " + cnt + " numbers of this card!");
        }
        if (!HearthStone.currentAccount.getUnlockedCards().contains(baseCard.getId())) {
            throw new HearthStoneException("This card is locked for you!");
        }
        if (baseCard.getHeroType() != HeroType.ALL && baseCard.getHeroType() != heroType) {
            throw new HearthStoneException("This card isn't for this hero!");
        }
        for (int i = 0; i < cnt; i++)
            cards.add(baseCard.copy());
    }

    public boolean canRemove(Card baseCard, int cnt) {
        return numberOfCards(baseCard) - cnt >= 0;
    }

    public void remove(Card baseCard, int cnt) throws Exception {
        if (numberOfCards(baseCard) - cnt < 0) {
            throw new HearthStoneException("There is not " + cnt + " numbers of " + baseCard.getName() + " in your deck!");
        }
        for (int i = 0; i < cnt; i++) {
            for (int j = 0; j < cards.size(); j++) {
                if (cards.get(j).getId() == baseCard.getId()) {
                    cards.remove(j);
                    break;
                }
            }
        }
    }

    public boolean isFull() {
        return cards.size() == HearthStone.maxCardInDeck;
    }

    public void cardPlay(Card baseCard) {
        cardGame.putIfAbsent(baseCard.getId(), 0);
        cardGame.put(baseCard.getId(), cardGame.get(baseCard.getId()) + 1);
    }
}