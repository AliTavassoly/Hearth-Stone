package hearthstone.model.heroes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.model.Collection;
import hearthstone.model.Deck;
import hearthstone.model.cards.Card;
import hearthstone.util.InterfaceAdapter;

import java.util.List;

public abstract class Hero{
    private int id;
    private String name;
    private String description;
    private int health;
    private HeroType type;
    private Collection collection;
    private Deck deck;

    public Hero(){ }

    public Hero(int id, String name, HeroType type, String description, int health, List<Integer> collection) throws Exception{
        this.id = id;
        this.name = name;
        this.description = description;
        this.health = health;
        this.type = type;

        for(int x : collection){
            this.collection.add(HearthStone.baseCards.get(x), 1);
        }
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

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Hero copy(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new InterfaceAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new InterfaceAdapter<Hero>());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this), Hero.class);
    }

}