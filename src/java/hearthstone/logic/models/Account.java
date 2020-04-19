package hearthstone.logic.models;

import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Account {
    private String username;
    private String name;
    private int id;

    private ArrayList<Hero> heroes = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();

    private Hero currentHero;
    private int gem;

    public Account() {

    }

    public Account(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
        gem = HearthStone.initialCoins;
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

    public Hero.Deck getCurrentDeck() throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        return currentHero.getSelectedDeck();
    }

    public void setCurrentHero(Hero currentHero) {
        this.currentHero = currentHero;
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getGem() {
        return gem;
    }

    public boolean canBuy(Card baseCard, int cnt) throws Exception {
        if (currentHero == null)
            return false;
        return cnt * baseCard.getBuyPrice() <= gem && currentHero.getCollection().canAddCollection(baseCard, cnt);
    }

    public boolean canSell(Card baseCard, int cnt) throws Exception {
        if (currentHero == null)
            return false;
        return currentHero.getCollection().canRemoveCollection(baseCard, cnt);
    }

    public void buyCards(Card baseCard, int cnt) throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        if (baseCard.getBuyPrice() * cnt > gem) {
            throw new HearthStoneException("Not enough coins!");
        }
        currentHero.getCollection().addCollection(baseCard, cnt);
        gem -= baseCard.getBuyPrice() * cnt;
        for (Hero hero : heroes) {
            if (hero.getType() != currentHero.getType() && (baseCard.getHeroType() == HeroType.ALL || baseCard.getHeroType() == hero.getType()))
                hero.getCollection().addCollection(baseCard, cnt);
        }
    }

    public void sellCards(Card baseCard, int cnt) throws Exception {
        if (currentHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        currentHero.getCollection().removeCollection(baseCard, cnt);
        currentHero.getSelectedDeck().removeDeck(baseCard, Math.min(cnt, currentHero.getSelectedDeck().numberInDeck(baseCard)));
        gem += baseCard.getSellPrice() * cnt;
        for (Hero hero : heroes) {
            if (hero.getType() != currentHero.getType() && (baseCard.getHeroType() == HeroType.ALL || baseCard.getHeroType() == hero.getType()))
                hero.getCollection().removeCollection(baseCard, cnt);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
