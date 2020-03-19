package hearthstone.models;

import hearthstone.HearthStone;
import hearthstone.models.cards.Card;
import hearthstone.models.heroes.Hero;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Account {
    private String username;
    private String name;
    private int id;

    private ArrayList<Hero> heroes = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();

    private Hero currentHero;
    private int coins;

    public Account() {

    }

    public Account(int id, String name, String username) throws CloneNotSupportedException {
        this.id = id;
        this.name = name;
        this.username = username;
        coins = HearthStone.initialCoins;

        for (Hero baseHero : HearthStone.baseHeroes.values()) {
            heroes.add(baseHero.copy());
        }
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

    public ArrayList<Card> getCurrentCollection() throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        return currentHero.getCollection();
    }

    public ArrayList<Card> getCurrentDeck() throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        return currentHero.getDeck();
    }

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

    public boolean canBuy(Card baseCard, int cnt) throws Exception {
        if (currentHero == null)
            return false;
        return cnt * baseCard.getBuyPrice() <= coins && currentHero.canAddCollection(baseCard, cnt);
    }

    public boolean canSell(Card baseCard, int cnt) throws Exception     {
        if (currentHero == null)
            return false;
        return currentHero.canRemoveCollection(baseCard, cnt);
    }

    public void buyCards(Card baseCard, int cnt) throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a here!");
        }
        if (baseCard.getBuyPrice() * cnt > coins) {
            throw new HearthStoneException("Not enough coins!");
        }
        try {
            currentHero.addCollection(baseCard, cnt);
            coins -= baseCard.getBuyPrice() * cnt;
        } catch (Exception e) {
            for (Hero hero : heroes) {
                if (hero.getType() != currentHero.getType())
                    hero.addCollection(baseCard, cnt);
            }
        }
    }

    public void sellCards(Card baseCard, int cnt) throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a here!");
        }
        try {
            currentHero.removeCollection(baseCard, cnt);
            coins += baseCard.getSellPrice() * cnt;
        } catch (Exception e) {
            for (Hero hero : heroes) {
                if (hero.getType() != currentHero.getType())
                    hero.removeCollection(baseCard, cnt);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
