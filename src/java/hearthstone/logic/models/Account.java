package hearthstone.logic.models;

import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Account {
    private String username;
    private String name;
    private int id;
    private ArrayList<Hero> heroes;
    private Collection collection;
    private ArrayList<Deck> decks;
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
        unlockedCards = new ArrayList<>();
        unlockedHeroes = new ArrayList<>();
        decks = new ArrayList<>();

        accountConfigs();
    }

    private void accountConfigs() {
        heroes.addAll(HearthStone.baseHeroes.values());

        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getId() % 2 == 0) {
                unlockedCards.add(card.getId());
            }
            cards.add(card);
        }
        for (int i = 0; i < HearthStone.baseHeroes.size(); i++) {
            unlockedHeroes.add(i);
        }

        collection = new Collection(cards);
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

    public Collection getCards() {
        return collection;
    }

    public void setCards(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
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

    public void setSelectedHero(Hero selectedHero) {
        this.selectedHero = selectedHero;
    }

    public Hero getSelectedHero() {
        if (selectedHero == null) {
            return null;
        }
        for (Hero hero : heroes) {
            if (selectedHero.getName().equals(hero.getName())) {
                return hero;
            }
        }
        return null;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getGem() {
        return gem;
    }

    public ArrayList<Deck> getDecks() {
        for (int i = 0; i < decks.size(); i++) {
            for (Hero hero : heroes) {
                for (int j = 0; j < hero.getDecks().size(); j++) {
                    Deck deck = decks.get(i);
                    Deck deck1 = hero.getDecks().get(j);
                    if (deck.getName().equals(deck1.getName()) && deck1.getHeroType() == deck.getHeroType()) {
                        decks.set(i, deck1);
                    }
                }
            }
        }
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    // End of setters and getters

    public boolean canBuy(Card baseCard, int cnt) {
        return unlockedHeroes.contains(Hero.getHeroByType(baseCard.getHeroType()).getId());
    }

    public boolean canSell(Card baseCard, int cnt) {
        return true;
    }

    public void buyCards(Card baseCard, int cnt) throws Exception {
        if (!unlockedCards.contains(baseCard.getId())) {
            throw new HearthStoneException("This card is locked for you!");
        }
        if (baseCard.getBuyPrice() * cnt > gem) {
            throw new HearthStoneException("Not enough gems!");
        }
        if (!unlockedHeroes.contains(Hero.getHeroByType(baseCard.getHeroType()).getId())) {
            throw new HearthStoneException("This card is for " +
                    Hero.getHeroByType(baseCard.getHeroType()).getName() + "class and you don't have it!"
            );
        }
        for (int i = 0; i < cnt; i++)
            collection.add(baseCard, cnt);
        gem -= baseCard.getSellPrice() * cnt;
    }

    public void sellCards(Card baseCard, int cnt) throws Exception {
        gem += baseCard.getSellPrice() * cnt;
        collection.remove(baseCard, cnt);
    }

    public ArrayList<Deck> getBestDecks(int cnt) {
        ArrayList<Deck> ans = new ArrayList<>();
        ArrayList<Deck> decks = getDecks();
        // Sort Decks

        cnt = Math.min(cnt, decks.size());
        for (int i = decks.size() - 1; i >= decks.size() - cnt; i--) {
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

    public void readyForPlay() throws Exception {
        if (HearthStone.currentAccount.getSelectedHero() == null) {
            throw new HearthStoneException("You should choose your hero first!");
        } else if (HearthStone.currentAccount.getSelectedHero().getSelectedDeck() == null) {
            throw new HearthStoneException("You should choose a deck for your hero!");
        } else {
            Player player = getPlayer();
            player.readyForPlay();
        }
    }

    public Player getPlayer() {
        return new Player(selectedHero, getSelectedHero().getSelectedDeck());
    }

    public class Collection {
        private ArrayList<Card> cards;

        public Collection() {
        }

        public Collection(ArrayList<Card> cards) {
            this.cards = cards;
        }

        public ArrayList<Card> getCards() {
            return cards;
        }

        public void setCards(ArrayList<Card> cards) {
            this.cards = cards;
        }

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
            return numberOfCards(baseCard) + cnt <= HearthStone.maxCardOfOneType && cards.size() + cnt <= HearthStone.maxCardInCollection;
        }

        public void add(Card baseCard, int cnt) throws Exception {
            if (numberOfCards(baseCard) + cnt > HearthStone.maxCardOfOneType) {
                throw new HearthStoneException("Can not have " + numberOfCards(baseCard) + cnt + " numbers of " + baseCard.getName() + " card!");
            }
            if (cards.size() + cnt > HearthStone.maxCardInCollection) {
                throw new HearthStoneException("Collection is full!");
            }
            for (int i = 0; i < cnt; i++)
                cards.add(baseCard.copy());
        }

        public boolean canRemove(Card baseCard, int cnt) {
            return numberOfCards(baseCard) - cnt >= 0;
        }

        public void remove(Card baseCard, int cnt) throws Exception {
            if (numberOfCards(baseCard) - cnt < 0) {
                throw new HearthStoneException("You don't have " + cnt + " numbers of " + baseCard.getName() + " in your collection!");
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
    }
}