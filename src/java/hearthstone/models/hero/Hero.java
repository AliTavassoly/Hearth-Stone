package hearthstone.models.hero;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hearthstone.server.data.ServerData;
import hearthstone.models.behaviours.Character;
import hearthstone.models.Deck;
import hearthstone.models.specialpower.SpecialHeroPower;
import hearthstone.util.HearthStoneException;
import hearthstone.util.jacksonserializers.DeckListSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public abstract class Hero implements HeroBehaviour, Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int allId;
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String heroPowerName;
    @Column
    private int health;
    @Column
    private int initialHealth;
    @Column
    private HeroType type;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonSerialize(converter = DeckListSerializer.class)
    private List<Deck> decks;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Deck selectedDeck;

    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    protected SpecialHeroPower specialHeroPower;

    @Transient
    @JsonProperty("immunities")
    private ArrayList<Integer> immunities;

    @Transient
    @JsonProperty("freezes")
    private ArrayList<Integer> freezes;

    @Transient
    @JsonProperty("isImmune")
    protected boolean isImmune;

    @Transient
    @JsonProperty("isFreeze")
    protected boolean isFreeze;

    @Transient
    @JsonProperty("spellSafe")
    protected boolean spellSafe;

    @Transient
    @JsonProperty("playerId")
    protected int playerId;

    @Transient
    @JsonProperty("heroGameId")
    protected int heroGameId;

    @PostLoad
    void postLoad() {
        this.decks = new ArrayList<>(this.decks);
    }

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
    public void setDecks(List<Deck> decks) {
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

    public void setHeroGameId(int heroGameId){
        this.heroGameId = heroGameId;
    }
    public int getHeroGameId(){
        return heroGameId;
    }

    public Hero copy() {
        ObjectMapper mapper = ServerData.getObjectCloneMapper();
        Hero hero = null;
        try {
            String json = mapper.writeValueAsString(this);
            hero = mapper.readValue(json, Hero.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hero.allId = 0;
        return hero;
    }

    // End of getter setter
    public Deck getDeckByName(String deckName){
        for(Deck deck: decks){
            if(deck.getName().equals(deckName))
                return deck;
        }
        return null;
    }

    public boolean validNameForDeck(String name){
        for(Deck deck: decks){
            if(deck.getName().equals(name))
                return false;
        }
        return true;
    }

    public void selectDeck(String deckName){
        Deck deck = getDeckByName(deckName);
        selectedDeck = deck;
    }

    public void removeDeck(String deckName){
        Deck deck = getDeckByName(deckName);
        if(selectedDeck != null && selectedDeck.getName().equals(deckName))
            selectedDeck = null;
        decks.remove(deck);
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
        //Mapper.reduceImmunities(getPlayerId(), this);
        this.reduceImmunities();

        //Mapper.handleImmunities(getPlayerId(), this);
        this.handleImmunities();

        //Mapper.reduceFreezes(getPlayerId(), this);
        this.reduceFreezes();

        //Mapper.handleFreezes(getPlayerId(), this);
        this.handleFreezes();
    }
}