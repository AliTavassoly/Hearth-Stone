package hearthstone.logic.models;

import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.*;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private Hero hero;
    private Deck originalDeck;
    private Deck deck;

    private int mana;
    private int turnNumber;

    private ArrayList<Card> hand;
    private ArrayList<Card> land;
    private Random random;

    private HeroPowerCard heroPower;

    private WeaponCard weapon;

    public Player(Hero hero, Deck deck) {
        this.hero = hero.copy();
        originalDeck = deck;
        this.deck = deck.copy();

        hand = new ArrayList<>();
        land = new ArrayList<>();
        random = new Random(System.currentTimeMillis());
        mana = 0;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getLand() {
        return land;
    }

    public void setLand(ArrayList<Card> land) {
        this.land = land;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public HeroPowerCard getHeroPower() {
        return heroPower;
    }

    public void setHeroPower(HeroPowerCard heroPower) {
        this.heroPower = heroPower;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    // End of getter setter

    public void readyForPlay() throws Exception{
        if(!deck.isFull())
            throw new HearthStoneException("You should complete your deck first!");
    }

    public void startGame() throws Exception{
        turnNumber = 1;
        mana = 1;

        pickCard();
        pickCard();
        pickCard();
    }

    public void pickCard() throws Exception{
        if(deck.getCards().size() == 0)
            throw new HearthStoneException("Game ended!");
        int cardInd = random.nextInt(deck.getCards().size());
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);
        if(hand.size() == GameConfigs.maxCardInHand)
            return;
        hand.add(card);

    }

    public void playCard(Card baseCard) throws Exception{
        if(baseCard.getManaCost() > mana)
            throw new HearthStoneException("you don't have enough mana!");
        Card cardInHand = null;

        for(int i = 0; i < hand.size(); i++){
            Card card = hand.get(i);
            if(card.getName().equals(baseCard.getName())){
                cardInHand = card;
            }
        }

        if(cardInHand instanceof HeroPowerCard){
            if(heroPower != null){
                throw new HearthStoneException("you are using hero power!");
            }
            heroPower = (HeroPowerCard) cardInHand;
        } else if(cardInHand instanceof SpellCard){

        } else if(cardInHand instanceof RewardCard){

        } else if(cardInHand instanceof WeaponCard){
            if(weapon != null){
                throw new HearthStoneException("you are using weapon!");
            }
            weapon = (WeaponCard) cardInHand;
        } else if(cardInHand instanceof MinionCard){
            if(land.size() == GameConfigs.maxCardInLand)
                throw new HearthStoneException("your land is full!");
            land.add(cardInHand);
        }

        hand.remove(cardInHand);
        mana -= cardInHand.getManaCost();

        for(Card card : originalDeck.getCards()){
            if(card.getName().equals(cardInHand.getName())){
                originalDeck.cardPlay(cardInHand);
            }
        }
    }

    public void startTurn() throws Exception{
        mana = ++turnNumber;
        mana = Math.min(mana, GameConfigs.maxManaInGame);
        pickCard();
    }
}
