package hearthstone.models;

import hearthstone.HearthStone;
import hearthstone.logic.GameConfigs;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.heroes.Mage;
import hearthstone.models.player.Player;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Account {
    @Id
    private String username;
    @Column
    private String name;
    @Column
    private int id;
    @Column
    private int cardsBackId;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    //@Transient
    private List<Hero> heroes;

    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Collection collection;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Deck> decks;

    @ElementCollection
    private List<Integer> unlockedCards;

    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Hero selectedHero;

    @Column
    private int gem;

    @PostLoad
    void postLoad() {
        this.heroes = new ArrayList<>(this.heroes);
        this.decks = new ArrayList<>(this.decks);
        this.unlockedCards = new ArrayList<>(this.unlockedCards);
    }

    public Account() {

    }

    public Account(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
        gem = GameConfigs.initialCoins;

        heroes = new ArrayList<>();
        unlockedCards = new ArrayList<>();
        decks = new ArrayList<>();

        accountConfigs();
    }

    private void accountConfigs() {
        for(Hero hero: HearthStone.baseHeroes.values()){
            Hero hero1 = hero.copy();
            heroes.add(hero1);
        }

        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getCardType() == CardType.HERO_POWER) {
                unlockedCards.add(card.getId());
                continue;
            }

            int number = 1 + Rand.getInstance().getRandomNumber(2);
            for (int i = 0; i < number; i++) {
                cards.add(card);
            }

            if (Rand.getInstance().getProbability(5, 6)) {
                unlockedCards.add(card.getId());
            }
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

    public List<Integer> getUnlockedCards() {
        return unlockedCards;
    }

    public void setUnlockedCards(List<Integer> unlockedCards) {
        this.unlockedCards = unlockedCards;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public List<Hero> getHeroes() {
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

    public List<Deck> getDecks() {
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

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public int getCardsBackId() {
        return cardsBackId;
    }

    public void setCardsBackId(int cardsBackId) {
        this.cardsBackId = cardsBackId;
    }

    // End of setters and getters

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
        for (int i = 0; i < cnt; i++)
            collection.add(baseCard, cnt);
        gem -= baseCard.getSellPrice() * cnt;

        hearthstone.util.Logger.saveLog("buy",
                "in market, bought " + 1 + " of " +
                        baseCard.getName() + "!");
    }

    public void sellCards(Card baseCard, int cnt) throws Exception {
        gem += baseCard.getSellPrice() * cnt;
        collection.remove(baseCard, cnt);

        hearthstone.util.Logger.saveLog("sell",
                "in market, sold " + 1 + " of " +
                        baseCard.getName() + "!");
    }

    public ArrayList<Deck> getBestDecks(int cnt) {
        ArrayList<Deck> ans = new ArrayList<>();
        List<Deck> decks = getDecks();

        Collections.sort(decks);

        cnt = Math.min(cnt, decks.size());
        for (int i = 0; i < cnt; i++) {
            ans.add(decks.get(i));
        }
        return ans;
    }

    public void unlockCard(int id) {
        unlockedCards.add(id);
    }

    public void readyForPlay() throws Exception {
        if (getSelectedHero() == null) {
            throw new HearthStoneException("You should choose your hero first!");
        } else if (getSelectedHero().getSelectedDeck() == null) {
            throw new HearthStoneException("You should choose a deck for your hero!");
        } else if (!getSelectedHero().getSelectedDeck().isFull()) {
            throw new HearthStoneException("You should complete your deck first!");
        }
    }

    public Player getPlayer() {
        return new Player(selectedHero, getSelectedHero().getSelectedDeck(), username);
    }
}