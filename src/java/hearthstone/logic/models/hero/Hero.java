package hearthstone.logic.models.hero;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.Character;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.card.Card;
import hearthstone.util.AbstractAdapter;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero implements HeroBehaviour, Character {
    private int id;
    private String name;
    private String description;
    private int health, initialHealth;
    private HeroType type;
    private ArrayList<Deck> decks;
    private Deck selectedDeck;

    private ArrayList<Integer> immunities;

    protected boolean isImmune;

    private int playerId;

    public Hero() {
        configHero();
    }

    public Hero(int id, String name, HeroType type, String description,
                int health, List<Integer> initialCardsId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.health = health;
        this.type = type;
        this.initialHealth = health;

        ArrayList<Card> initialCard = new ArrayList<>();

        for (int x : initialCardsId) {
            initialCard.add(HearthStone.baseCards.get(x).copy());
        }

        configHero();
    }

    private void configHero(){
        immunities = new ArrayList<>();

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


    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    public boolean isImmune() {
        return isImmune;
    }
    public void setImmune(boolean immune) {
        isImmune = immune;
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

    public void reduceImmunities(){
        for(int i = 0; i < immunities.size(); i++){
            immunities.set(i, immunities.get(i) - 1);
            if(immunities.get(i) <= 0){
                immunities.remove(i);
                i--;
            }
        }
    }

    public void handleImmunities(){
        if(immunities.size() > 0)
            isImmune = true;
        else
            isImmune = false;
    }

    public void addImmunity(int numberOfTurn){
        immunities.add(numberOfTurn);
    }

    public Hero copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Hero.class), Hero.class);
    }

    @Override
    public void gotDamage(int damage) throws HearthStoneException {
        if(isImmune){
            throw new HearthStoneException("The Hero is Immune!");
        }
        this.health -= damage;
    }

    @Override
    public void gotHeal(int heal) {
        this.health += heal;
    }

    @Override
    public void restoreHealth(int heal) {
        health = Math.min(initialHealth, health + heal);
    }

    @Override
    public void startTurnBehave() {
        reduceImmunities();
        handleImmunities();
    }
}