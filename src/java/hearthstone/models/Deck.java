package hearthstone.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.HearthStone;
import hearthstone.data.Data;
import hearthstone.logic.GameConfigs;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Deck implements Comparable<Deck> {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
    @Column
    private int totalGames;
    @Column
    private int winGames;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Card> cards = new ArrayList<>();

    @ElementCollection()
    @MapKeyColumn(name="Deck_CardGame")
    private Map<Integer, Integer> cardGame = new HashMap<>();

    @Column
    private HeroType heroType;

    @PostLoad
    void postLoad() {
        this.cardGame = new HashMap<>(this.cardGame);
        this.cards = new ArrayList<>(this.cards);
    }

    public Deck() {
    }

    public Deck(String name, HeroType heroType) {
        this.name = name;
        this.heroType = heroType;
    }

    public Deck(String name, HeroType heroType, ArrayList<Card> cards){
        this.name = name;
        this.heroType = heroType;

        this.cards = cards;
    }

    public Map<Integer, Integer> getCardGame() {
        return cardGame;
    }

    public void setCardGame(Map<Integer, Integer> cardGame) {
        this.cardGame = cardGame;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
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

    public int getWinPercentage() {
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

        if (!cardGame.get(card1.getId()).equals(cardGame.get(card2.getId())))
            return cardGame.get(card1.getId()) > cardGame.get(card2.getId()) ? card1 : card2;
        if (card1.getCardType() != CardType.HERO_POWER && card2.getCardType() != CardType.HERO_POWER) {
            if (card1.getRarity().getValue() != card2.getRarity().getValue())
                return card1.getRarity().getValue() > card2.getRarity().getValue() ? card1 : card2;
        }
        if (card1.getManaCost() != card2.getManaCost())
            return card1.getManaCost() > card2.getManaCost() ? card1 : card2;
        if (card1.getCardType() == CardType.MINION_CARD || card2.getCardType() == CardType.MINION_CARD)
            return card1.getCardType() == CardType.MINION_CARD ? card1 : card2;
        return card1;
    }

    public String getBestCardName() {
        if (cards.size() == 0)
            return null;
        Card mxCard = cards.get(0).copy();
        for (int i = 1; i < cards.size(); i++) {
            Card card = cards.get(i).copy();
            mxCard = biggerCard(mxCard, card);
        }
        return mxCard.getName();
    }

    public Deck copy() {
        ObjectMapper mapper = Data.getObjectCloneMapper();
        Deck deck = null;
        try {
            deck = mapper.readValue(mapper.writeValueAsString(this), Deck.class);
        } catch (Exception e) { }
        return deck;
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
        if (cards.size() + cnt > GameConfigs.maxCardInDeck) {
            return false;
        }
        if (numberOfCards(baseCard) + cnt > GameConfigs.maxCardOfOneType) {
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

    public void add(Card baseCard, int cnt) throws HearthStoneException {
        if (numberOfCards(baseCard) + cnt > HearthStone.currentAccount.getCollection().numberOfCards(baseCard)) {
            throw new HearthStoneException("You don't have " + (numberOfCards(baseCard) + cnt) + " numbers of this card!");
        }
        if (cards.size() + cnt > GameConfigs.maxCardInDeck) {
            throw new HearthStoneException("Deck is full!");
        }
        if (numberOfCards(baseCard) + cnt > GameConfigs.maxCardOfOneType) {
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

    public void addInTheMiddleOfGame(Card card) throws HearthStoneException {
        if (cards.size() + 1 > GameConfigs.maxCardInDeck) {
            throw new HearthStoneException("Deck is full!");
        }
        cards.add(card);
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
        return cards.size() == GameConfigs.maxCardInDeck;
    }

    public void cardPlay(int cardId) {
        cardGame.putIfAbsent(cardId, 0);
        cardGame.put(cardId, cardGame.get(cardId) + 1);
    }

    @Override
    public int compareTo(Deck deck) {
        if (this.getWinPercentage() != deck.getWinPercentage()) {
            if (this.getWinPercentage() > deck.getWinPercentage())
                return -1;
            return 1;
        }
        if(this.getWinGames() != deck.getWinGames()){
            if (this.getWinGames() > deck.getWinGames())
                return -1;
            return 1;
        }
        if(this.getTotalGames() != deck.getTotalGames()){
            if (this.getTotalGames() > deck.getTotalGames())
                return -1;
            return 1;
        }
        if(this.getManaAv() != deck.getManaAv()){
            if(this.getManaAv() < deck.getManaAv())
                return -1;
            return 1;
        }
        return 0;
    }
}