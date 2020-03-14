package hearthstone.data.bean;

import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.data.bean.heroes.HeroType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Account {
    private int password;
    private String username;
    private String name;
    private int id;
    private ArrayList<Hero> heroes;
    private ArrayList<Card> cards;
    private Hero currentHero;
    public Map<HeroType, Collection> heroCollection = new HashMap<>();
    public Map<HeroType, Deck> heroDeck = new HashMap<>();
    private int coins;

    Account(){
        coins = 50;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setHeroes(ArrayList<Hero> heroes){
        this.heroes = heroes;
    }
    public ArrayList<Hero> getHeroes(){
        return heroes;
    }

    public void setBaseCards(ArrayList<Card> cards){
        this.cards = cards;
    }
    public ArrayList<Card> getBaseCards(){
        return cards;
    }

    public Collection getCurrentCollection(){ return heroCollection.get(currentHero.getType()); }
    //public Collection getCollection(Hero hero){ return heroCollection.get(hero.getType()); }

    public Deck getCurrentDeck(){ return heroDeck.get(currentHero.getType()); }
    //public Deck getDeck(Hero hero){ return heroDeck.get(hero.getType()); }

    public void setCurrentHero(Hero currentHero){
        this.currentHero = currentHero;
    }
    public Hero getCurrentHero(){
        return currentHero;
    }

    public void setCoins(int coins){
        this.coins = coins;
    }
    public int getCoins(){
        return coins;
    }

    public boolean canAddCard(Card card, int cnt){
        return getCurrentCollection().canAdd(card, cnt);
    }
    public void addCard(Card card, int cnt){
        for(Hero hero : heroes){
            if(heroCollection.get(hero.getType()).canAdd(card, cnt))
                heroCollection.get(hero.getType()).add(card, cnt);
        }
    }

    public boolean canRemoveCard(Card card, int cnt){
        return getCurrentCollection().canRemove(card, cnt);
    }
    public void removeCard(Card card, int cnt){
        for(Hero hero : heroes){
            if(heroCollection.get(hero.getType()).canRemove(card, cnt)){
                heroCollection.get(hero.getType()).remove(card, cnt);
            }
        }
    }

    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
