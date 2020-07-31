package hearthstone.models.hero;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.behaviours.Character;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.passive.Passive;
import hearthstone.models.specialpower.SpecialHeroPower;
import hearthstone.util.AbstractAdapter;
import hearthstone.util.HearthStoneException;
import jdk.jfr.Name;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public abstract class Hero implements HeroBehaviour, Character {
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String heroPowerName;
    @Transient
    private int health;
    @Column
    private int initialHealth;
    @Column
    private HeroType type;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Deck> decks;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Deck selectedDeck;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    protected SpecialHeroPower specialHeroPower;

    @Transient
    private ArrayList<Integer> immunities;

    @Transient
    private ArrayList<Integer> freezes;

    @Transient
    protected boolean isImmune;

    @Transient
    protected boolean isFreeze;

    @Transient
    protected boolean spellSafe;

    @Transient
    private int playerId;

    public Hero() {
        configHero();
    }

    public Hero(int id, String name, HeroType type, String description,
                String heroPowerName,
                int health) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.heroPowerName = heroPowerName;
        this.health = health;
        this.type = type;
        this.initialHealth = health;

        configHero();
    }

    private void configHero(){
        immunities = new ArrayList<>();
        freezes = new ArrayList<>();
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

    public String getHeroPowerName() {
        return heroPowerName;
    }
    public void setHeroPowerName(String heroPowerName) {
        this.heroPowerName = heroPowerName;
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

    public List<Deck> getDecks() {
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

    public SpecialHeroPower getSpecialHeroPower() { return specialHeroPower; }
    public void setSpecialHeroPower(SpecialHeroPower specialHeroPower) { this.specialHeroPower = specialHeroPower; }

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

    public boolean isFreeze() {
        return isFreeze;
    }
    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    public boolean isSpellSafe() {
        return spellSafe;
    }
    public void setSpellSafe(boolean spellSafe) {
        this.spellSafe = spellSafe;
    }

    public Hero copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        gsonBuilder.registerTypeAdapter(Passive.class, new AbstractAdapter<Passive>());
        gsonBuilder.registerTypeAdapter(SpecialHeroPower.class, new AbstractAdapter<SpecialHeroPower>());

        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Hero.class), Hero.class);
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

    @Override
    public void reduceImmunities(){
        for(int i = 0; i < immunities.size(); i++){
            immunities.set(i, immunities.get(i) - 1);
            if(immunities.get(i) <= 0){
                immunities.remove(i);
                i--;
            }
        }
    }

    @Override
    public void reduceFreezes(){
        for(int i = 0; i < freezes.size(); i++){
            freezes.set(i, freezes.get(i) - 1);
            if(freezes.get(i) <= 0){
                freezes.remove(i);
                i--;
            }
        }
    }

    @Override
    public void handleImmunities(){
        if(immunities.size() > 0)
            isImmune = true;
        else
            isImmune = false;
    }

    @Override
    public void handleFreezes(){
        if(freezes.size() > 0)
            isFreeze = true;
        else
            isFreeze = false;
    }

    @Override
    public void addImmunity(int numberOfTurn){
        immunities.add(numberOfTurn);
    }

    @Override
    public void addFreezes(int numberOfTurn) {
        freezes.add(numberOfTurn);
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
    public void restoreHealth() {
        this.health = initialHealth;
    }

    @Override
    public void restoreHealth(int heal) {
        health = Math.min(initialHealth, health + heal);
    }

    @Override
    public void startTurnBehave()  {
        Mapper.getInstance().reduceImmunities(getPlayerId(), this);
        Mapper.getInstance().handleImmunities(getPlayerId(), this);

        Mapper.getInstance().reduceFreezes(getPlayerId(), this);
        Mapper.getInstance().handleFreezes(getPlayerId(), this);
    }
}