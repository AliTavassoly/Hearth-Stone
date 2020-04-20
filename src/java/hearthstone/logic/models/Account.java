package hearthstone.logic.models;

import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Account {
    private String username;
    private String name;
    private int id;
    private ArrayList<Hero> heroes;
    private ArrayList<Card> cards;
    private ArrayList<Hero.Deck> decks;
    private ArrayList<Integer> unlockedCards;
    private ArrayList<Integer> unlockedHeroes;

    private Hero selectedHero;
    private int gem;

    public Account() {

    }

    public Account(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
        gem = HearthStone.initialCoins;

        heroes = new ArrayList<>();
        cards = new ArrayList<>();
        unlockedCards = new ArrayList<>();
        unlockedHeroes = new ArrayList<>();
        decks = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Integer> getUnlockedCards() {
        return unlockedCards;
    }

    public void setUnlockedCards(ArrayList<Integer> unlockedCards) {
        this.unlockedCards = unlockedCards;
    }

    public ArrayList<Integer> getUnlockedHeroes() {
        return unlockedHeroes;
    }

    public void setUnlockedHeroes(ArrayList<Integer> unlockedHeroes) {
        this.unlockedHeroes = unlockedHeroes;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        this.heroes = heroes;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public Hero.Deck getCurrentDeck() throws Exception {
        if (selectedHero == null) {
            throw new HearthStoneException("You did not choose a hero!");
        }
        return selectedHero.getSelectedDeck();
    }

    public void setSelectedHero(Hero selectedHero) {
        this.selectedHero = selectedHero;
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getGem() {
        return gem;
    }

    public ArrayList<Hero.Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Hero.Deck> decks) {
        this.decks = decks;
    }

    // End of setters and getters

    public boolean canBuy(Card baseCard, int cnt) throws Exception {
        return unlockedHeroes.contains(Hero.getHeroByType(baseCard.getHeroType()).getId());
    }

    public boolean canSell(Card baseCard, int cnt) throws Exception {
        for (Hero hero : heroes) {
            if (baseCard.getHeroType() == HeroType.ALL || hero.getType() == baseCard.getHeroType()) {
                if (!hero.getCollection().canRemove(baseCard, cnt)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void buyCards(Card baseCard, int cnt) throws Exception {
        if (!unlockedCards.contains(baseCard.getId())) {
            throw new HearthStoneException("This card is lock for you!");
        }
        if (baseCard.getBuyPrice() * cnt > gem) {
            throw new HearthStoneException("Not enough gems!");
        }
        if (!unlockedHeroes.contains(Hero.getHeroByType(baseCard.getHeroType()).getId())) {
            throw new HearthStoneException("This card is for " +
                    Hero.getHeroByType(baseCard.getHeroType()).getName() + "class and you don't have it!"
            );
        }

        if (baseCard.getHeroType() == HeroType.ALL) {
            for (Hero hero : heroes) {
                hero.getCollection().add(baseCard, cnt);
            }
        } else {
            Hero heroOfThisCard = null;
            for (Hero hero : heroes) {
                if (hero.getType() == baseCard.getHeroType()) {
                    heroOfThisCard = hero;
                    break;
                }
            }
            heroOfThisCard.getCollection().add(baseCard, cnt);
        }
        gem -= baseCard.getSellPrice() * cnt;
        for(int i = 0; i < cnt; i++){
            cards.add(baseCard.copy());
        }
    }

    public void sellCards(Card baseCard, int cnt) throws Exception {
        if (baseCard.getHeroType() == HeroType.ALL) {
            for (Hero hero : heroes) {
                for(Hero.Deck deck : hero.getDecks()){
                    if(deck.numberOfCards(baseCard) > hero.getCollection().numberOfCards(baseCard)){
                       deck.remove(baseCard,
                               deck.numberOfCards(baseCard) - hero.getCollection().numberOfCards(baseCard));
                    }
                }
                hero.getCollection().remove(baseCard, cnt);
            }
        } else {
            Hero heroOfThisCard = null;
            for (Hero hero : heroes) {
                if (hero.getType() == baseCard.getHeroType()) {
                    heroOfThisCard = hero;
                    break;
                }
            }
            for(Hero.Deck deck : heroOfThisCard.getDecks()) {
                if (deck.numberOfCards(baseCard) > heroOfThisCard.getCollection().numberOfCards(baseCard)) {
                    deck.remove(baseCard,
                            deck.numberOfCards(baseCard) -
                                    heroOfThisCard.getCollection().numberOfCards(baseCard));
                }
            }
            heroOfThisCard.getCollection().remove(baseCard, cnt);
        }

        for(int i = 0; i < cnt; i++){
            for(int j = 0; j < cards.size(); j++){
                if(cards.get(j).getId() == baseCard.getId()){
                    cards.remove(j);
                    break;
                }
            }
        }
        gem += baseCard.getSellPrice() * cnt;
    }

    public ArrayList<Hero.Deck> getBestDecks(int cnt){
        ArrayList<Hero.Deck> ans = new ArrayList<>();

        // Sort Decks

        cnt = Math.min(cnt, decks.size());
        for(int i = decks.size() - 1; i >= decks.size() - cnt; i--){
            ans.add(decks.get(i));
        }
        return ans;
    }

    public void unlockHero(int id) {
        unlockedHeroes.add(id);
    }

    public void unlockCard(int id) {
        unlockedCards.add(id);
    }
}