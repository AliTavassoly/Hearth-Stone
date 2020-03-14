package hearthstone.credentials;

import hearthstone.game.Collection;
import hearthstone.game.Deck;
import hearthstone.game.cards.Card;
import hearthstone.game.heroes.Hero;
import hearthstone.game.heroes.HeroType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class User {
    private String username, password, name;
    private int id;
    private ArrayList<Hero> heroes;
    private ArrayList<Card> cards;
    private Hero currentHero;
    public Map<HeroType, Collection> heroCollection = new HashMap<>();
    public Map<HeroType, Deck> heroDeck = new HashMap<>();
    private int coins;

    User(){
        coins = 50;

    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
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
            if(heroCollection.get(hero).canAdd(card, cnt))
                heroCollection.get(hero).add(card, cnt);
        }
    }

    public boolean canRemoveCard(Card card, int cnt){
        return getCurrentCollection().canRemove(card, cnt);
    }
    public void removeCard(Card card, int cnt){
        for(Hero hero : heroes){
            if(heroCollection.get(hero).canRemove(card, cnt)){
                heroCollection.get(hero).remove(card, cnt);
            }
        }
    }

}
