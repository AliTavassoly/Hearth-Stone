package hearthstone.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;
import hearthstone.shared.GameConfigs;
import hearthstone.util.jacksonserializers.DeckListSerializer;
import hearthstone.util.HearthStoneException;
import hearthstone.util.jacksonserializers.HeroListSerializer;
import hearthstone.util.Rand;
import hearthstone.util.jacksonserializers.IntegerListSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(value = {"player", "allDecks"})
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
    @Column
    private int cup;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonSerialize(converter = HeroListSerializer.class)
    private List<Hero> heroes;

    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Collection collection;

    @ElementCollection
    @JsonSerialize(converter = IntegerListSerializer.class)
    private List<Integer> unlockedCards;

    @ElementCollection
    @JsonSerialize(converter = IntegerListSerializer.class)
    private List<Integer> gamesCup;

    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Hero selectedHero;

    @Column
    private int gem;

    @PostLoad
    void postLoad() {
        this.heroes = new ArrayList<>(this.heroes);
        this.unlockedCards = new ArrayList<>(this.unlockedCards);
        this.gamesCup = new ArrayList<>(this.gamesCup);
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
        gamesCup = new ArrayList<>();

        accountConfigs();
    }

    private void accountConfigs() {
        for (Hero hero : ServerData.baseHeroes.values()) {
            Hero hero1 = hero.copy();
            heroes.add(hero1);
        }

        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : ServerData.baseCards.values()) {
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

    public int getCardsBackId() {
        return cardsBackId;
    }

    public void setCardsBackId(int cardsBackId) {
        this.cardsBackId = cardsBackId;
    }

    public int getCup(){
        return cup;
    }

    public void setCup(int cup){
        this.cup = cup;
    }
    // End of setters and getters

    public void selectHero(String heroName) {
        for (Hero hero : heroes) {
            if (hero.getName().equals(heroName)) {
                selectedHero = hero;
                return;
            }
        }
    }

    public boolean canSell(Card baseCard, int cnt) {
        return true;
    }

    public void buyCards(Card baseCard, int cnt) throws HearthStoneException {
        if (!unlockedCards.contains(baseCard.getId())) {
            throw new HearthStoneException("This card is locked for you!");
        }
        if (baseCard.getBuyPrice() * cnt > gem) {
            throw new HearthStoneException("Not enough gems!");
        }
        for (int i = 0; i < cnt; i++)
            collection.add(baseCard, cnt);
        gem -= baseCard.getSellPrice() * cnt;

        try {
            hearthstone.util.Logger.saveLog("buy",
                    "in market, bought " + 1 + " of " +
                            baseCard.getName() + "!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sellCards(Card baseCard, int cnt) throws HearthStoneException {
        gem += baseCard.getSellPrice() * cnt;
        collection.remove(baseCard, cnt);

        try {
            hearthstone.util.Logger.saveLog("sell",
                    "in market, sold " + 1 + " of " +
                            baseCard.getName() + "!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Deck> getBestDecks(int cnt) {
        ArrayList<Deck> ans = new ArrayList<>();
        List<Deck> decks = new ArrayList<>();

        for(Deck deck: getAllDecks()){
            decks.add(deck);
        }

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

    private ArrayList<Deck> getAllDecks(){
        ArrayList<Deck> decks = new ArrayList<>();
        for(Hero hero: heroes){
            for(Deck deck: hero.getDecks()){
                decks.add(deck);
            }
        }
        return decks;
    }

    public Hero getHeroByName(String heroName) {
        for (Hero hero : heroes) {
            if (hero.getName().equals(heroName))
                return hero;
        }
        return null;
    }

    public void createDeck(Deck deck, String heroName) throws HearthStoneException {
        Hero hero = getHeroByName(heroName);
        if (!hero.validNameForDeck(deck.getName()))
            throw new HearthStoneException("This name is already token!");
        if (GameConfigs.maxNumberOfDeck == hero.getDecks().size())
            throw new HearthStoneException("You can't have " + GameConfigs.maxNumberOfDeck +
                    " number of decks for one hero!");

        List<Deck> decks = hero.getDecks();
        decks.add(deck);

        //this.decks.add(deck);

        hero.setDecks(decks);
    }

    public void selectDeck(String heroName, String deckName) {
        Hero hero = getHeroByName(heroName);
        hero.selectDeck(deckName);
    }

    public void removeDeck(String heroName, String deckName) {
        Hero hero = getHeroByName(heroName);
        hero.removeDeck(deckName);
        /*for(Deck deck: getAllDecks()){
            if(deck.getName().equals(deckName) && deck.getHeroType().getHeroName().equals(heroName)){
                this.decks.remove(deck);
                break;
            }
        }*/
    }

    public Card addCardToDeck(String heroName, String deckName, int cardId,
                              Collection accountCollection, List<Integer> unlockedCards, int cnt) throws HearthStoneException{
        Hero hero = getHeroByName(heroName);
        Deck deck = hero.getDeckByName(deckName);

        Card card = ServerData.getCardById(cardId);
        deck.add(card, accountCollection, unlockedCards, cnt);

        return card;
    }

    public Card removeCardFromDeck(String heroName, String deckName, int cardId, int cnt) throws HearthStoneException{
        Hero hero = getHeroByName(heroName);
        Deck deck = hero.getDeckByName(deckName);

        Card card = ServerData.getCardById(cardId);
        deck.remove(card, cnt);

        return card;
    }

    public void lostGame(String heroName, String deckName, int enemyCup, boolean updateDeck) {
        int cupDiff = -(g(this.cup + enemyCup) + f(this.cup - enemyCup));

        this.cup += cupDiff;
        this.cup = Math.max(0, this.cup);

        if(updateDeck) {
            Hero hero = getHeroByName(heroName);
            Deck deck = hero.getDeckByName(deckName);

            deck.lostGame(cupDiff);
        }
    }

    public void wonGame(String heroName, String deckName, int enemyCup, boolean updateDck) {
        int cupDiff = g(this.cup + enemyCup) + f(this.cup - enemyCup);

        this.cup += cupDiff;
        this.cup = Math.max(0, this.cup);

        if(updateDck) {
            Hero hero = getHeroByName(heroName);
            Deck deck = hero.getDeckByName(deckName);

            deck.wonGame(cupDiff);
        }
    }

    public int f(int x){
        if(x >= 0){
            return (int)((double)2 * Math.sqrt(x));
        } else {
            return -(int)((double)2 * Math.sqrt(x));
        }
    }

    public int g(int x){
        return (int)((double)20 / ((double) x / (double) 200 + (double) 1) + (double) 30);
    }
}