package hearthstone.data.bean;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Account {
    private String username;
    private String name;
    private int id;
    private ArrayList<Hero> heroes;
    private ArrayList<Card> cards;
    private Hero currentHero;
    private Map<HeroType, Collection> heroCollection = new HashMap<>();
    private Map<HeroType, Deck> heroDeck = new HashMap<>();
    private int coins;

    public Account() {
        coins = HearthStone.initialCoins;
    }

    public Account(int id, String name, String username){
        this.id = id;
        this.name = name;
        this.username = username;
        coins = HearthStone.initialCoins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        this.heroes = heroes;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public void setBaseCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getBaseCards() {
        return cards;
    }

    public Collection getCurrentCollection() {
        return heroCollection.get(currentHero.getType());
    }
    //public Collection getCollection(Hero hero){ return heroCollection.get(hero.getType()); }

    public Deck getCurrentDeck() {
        return heroDeck.get(currentHero.getType());
    }
    //public Deck getDeck(Hero hero){ return heroDeck.get(hero.getType()); }

    public void setCurrentHero(Hero currentHero) {
        this.currentHero = currentHero;
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public boolean canBuy(Card card, int cnt) {
        return cnt * card.getBuyPrice() <= coins && getCurrentCollection().canAdd(card, cnt);
    }

    public boolean canSell(Card card, int cnt){
        return getCurrentCollection().canRemove(card, cnt);
    }

    public void buyCards(Card card, int cnt) throws Exception {
        if (card.getBuyPrice() * cnt > coins) {
            throw new HearthStoneException("Not enough coins !");
        }
        for (Hero hero : heroes) {
            heroCollection.get(hero.getType()).add(card, cnt);
        }
        coins -= card.getBuyPrice() * cnt;
    }

    public void sellCards(Card card, int cnt) throws Exception {
        for (Hero hero : heroes) {
            heroCollection.get(hero.getType()).remove(card, cnt);
        }
        coins += card.getSellPrice() * cnt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
