package hearthstone.logic.models.hero;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.MinionCard;
import hearthstone.util.HearthStoneException;
import hearthstone.util.AbstractAdapter;

import javax.print.DocFlavor;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Hero {
    private int id;
    private String name;
    private String description;
    private int health;
    private HeroType type;
    private Collection collection;
    private ArrayList<Deck> decks;
    private Deck selectedDeck;

    public Hero() {
    }

    public Hero(int id, String name, HeroType type, String description,
                int health, List<Integer> initialCardsId) throws Exception {
        this.id = id;
        this.name = name;
        this.description = description;
        this.health = health;
        this.type = type;

        ArrayList<Card> initialCard = new ArrayList<>();

        for (int x : initialCardsId) {
            initialCard.add(HearthStone.baseCards.get(x).copy());
        }

        decks = new ArrayList<>();
        collection = new Collection(initialCard);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public HeroType getType() {
        return type;
    }

    public void setType(HeroType type) {
        this.type = type;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public Deck getSelectedDeck(){
        return selectedDeck;
    }

    public void setSelectedDeck(Deck selectedDeck){
        this.selectedDeck = selectedDeck;
    }

    // End of getter setter

    public Hero copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Hero.class), Hero.class);
    }

    public class Deck {
        private String name;
        private int totalGame;
        private int winGame;
        private HashMap<Integer, Integer> cardGame;
        private ArrayList<Card> cards = new ArrayList<>();

        public Deck(){ }

        public Deck(String name){
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

        public Hero getHero() {
            return Hero.this;
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

        //End of getter setter !

        public int numberInDeck(Card baseCard){
            int ans = 0;
            for(Card card : cards){
                if(card.getId() == baseCard.getId()){
                    ans++;
                }
            }
            return ans;
        }

        public boolean canAddDeck(Card baseCard, int cnt) {
            if (cards.size() + cnt > HearthStone.maxDeckSize) {
                return false;
            }
            if (baseCard.getHeroType()!= HeroType.ALL && baseCard.getHeroType()!= Hero.this.getType()) {
                return false;
            }
            if (numberInDeck(baseCard) + cnt > HearthStone.maxNumberOfCard) {
                return false;
            }
            return true;
        }

        public void addDeck(Card baseCard, int cnt) throws Exception {
            if (cards.size() + cnt > HearthStone.maxDeckSize) {
                throw new HearthStoneException("Not enough space in your deck!");
            }
            if (baseCard.getHeroType()!= HeroType.ALL && baseCard.getHeroType()!= Hero.this.getType()) {
                throw new HearthStoneException("Hero does not match!");
            }
            if (numberInDeck(baseCard) + cnt > HearthStone.maxNumberOfCard) {
                throw new HearthStoneException("You can not have " + cnt + " number of this card!");
            }
            for(int i = 0; i < cnt; i++)
                cards.add(baseCard.copy());
        }

        public boolean canRemoveDeck(Card baseCard, int cnt) {
            return numberInDeck(baseCard) - cnt >= 0;
        }

        public void removeDeck(Card baseCard, int cnt) throws Exception{
            if (numberInDeck(baseCard) - cnt < 0) {
                throw new HearthStoneException("There is not " + cnt + " number of " + baseCard.getName() + " in your deck!");
            }
            for(int i = 0; i < cnt; i++){
                for(int j = 0; j < cards.size(); j++){
                    if(cards.get(j).getId() == baseCard.getId()){
                        cards.remove(j);
                        break;
                    }
                }
            }
        }
    }

    public class Collection{
        private ArrayList<Card> cards;

        public Collection(){ }

        public Collection(ArrayList<Card> cards){
            this.cards = cards;
        }

        public ArrayList<Card> getCards(){
            return cards;
        }

        public void setCards(ArrayList<Card> cards){
            this.cards = cards;
        }

        public int numberInCollection(Card baseCard){
            int ans = 0;
            for(Card card : cards){
                if(card.getId() == baseCard.getId()){
                    ans++;
                }
            }
            return ans;
        }

        public boolean canAddCollection(Card baseCard, int cnt) {
            if (baseCard.getHeroType()!= HeroType.ALL && baseCard.getHeroType()!= Hero.this.getType()) {
                return false;
            }
            return numberInCollection(baseCard) + cnt <= HearthStone.maxNumberOfCard && cards.size() + cnt <= HearthStone.maxCollectionSize;
        }

        public void addCollection(Card baseCard, int cnt) throws Exception {
            if (baseCard.getHeroType()!= HeroType.ALL && baseCard.getHeroType()!= Hero.this.getType()) {
                throw new HearthStoneException("Hero does not match!");
            }
            if(numberInCollection(baseCard) + cnt > HearthStone.maxNumberOfCard){
                System.out.println(numberInCollection(baseCard) + " " + HearthStone.maxNumberOfCard);
                throw new HearthStoneException("Can not have " + numberInCollection(baseCard) + cnt + " number of " + baseCard.getName() + " card!");
            }
            if(cards.size() + cnt > HearthStone.maxCollectionSize){
                throw new HearthStoneException("Not enough space!");
            }
            for(int i = 0; i < cnt; i++)
                cards.add(baseCard.copy());
        }

        public boolean canRemoveCollection(Card baseCard, int cnt){
            return numberInCollection(baseCard) - cnt >= 0;
        }

        public void removeCollection(Card baseCard, int cnt) throws Exception {
            if (numberInCollection(baseCard) - cnt < 0) {
                throw new HearthStoneException("There is not " + cnt + " number of " + baseCard.getName() + " in your collection!");
            }
            for(int i = 0; i < cnt; i++){
                for(int j = 0; j < cards.size(); j++){
                    if(cards.get(j).getId() == baseCard.getId()){
                        cards.remove(j);
                        break;
                    }
                }
            }
        }
    }
}