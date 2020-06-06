package hearthstone.models.hero;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.logic.GameConfigs;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.util.AbstractAdapter;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero {
    private int id;
    private String name;
    private String description;
    private int health;
    private HeroType type;
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

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public Deck getSelectedDeck() {
        if(selectedDeck == null)
            return null;
        for(Deck deck : decks){
            if(deck.getName().equals(selectedDeck.getName())){
                return deck;
            }
        }
        return null;
    }

    public void setSelectedDeck(Deck selectedDeck){
        this.selectedDeck = selectedDeck;
    }

    // End of getter setter
    public void makeNewDeck(Deck deck) throws Exception{
        if(GameConfigs.maxNumberOfDeck == decks.size()){
            throw new HearthStoneException("You can't have " + GameConfigs.maxNumberOfDeck +
                    " number of decks for one hero!");
        }
        decks.add(deck);
    }

    public static Hero getHeroByType(HeroType heroType){
        for(Hero hero : HearthStone.baseHeroes.values()){
            if(hero.getType() == heroType)
                return hero.copy();
        }
        return null;
    }

    public Hero copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Hero.class), Hero.class);
    }
}