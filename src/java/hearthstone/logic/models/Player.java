package hearthstone.logic.models;

import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.*;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;

import java.util.ArrayList;

public class Player {
    private Hero hero;
    private final Deck originalDeck;
    private Deck deck;
    private Passive passive;

    private int mana;
    private int turnNumber;

    private boolean isStarted;

    private ArrayList<Card> hand;
    private ArrayList<Card> land;

    private HeroPowerCard heroPower;

    private WeaponCard weapon;

    public Player(Hero hero, Deck deck) {
        this.hero = hero.copy();
        originalDeck = deck;
        this.deck = deck.copy();

        hand = new ArrayList<>();
        land = new ArrayList<>();
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

    public Passive getPassive() {
        return passive;
    }

    public void setPassive(Passive passive) {
        this.passive = passive;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    // End of getter setter

    public void doPassives() {
        if (passive.getName().equals("Off Cards")) {
            for (Card card : deck.getCards()) {
                card.setManaCost(Math.max(0, card.getManaCost() - 1));
            }
        } else if (passive.getName().equals("Free Power")) {
            for (Card card : deck.getCards()) {
                if (card instanceof HeroPowerCard)
                    card.setManaCost(Math.max(0, card.getManaCost() - 1));
            }
        }
    }

    public void startGame() throws Exception {
        turnNumber = 0;
        mana = 0;

        pickCard();
        pickCard();
        pickCard();
    }

    public void pickCard() throws Exception {
        if (deck.getCards().size() == 0)
            throw new HearthStoneException("Game ended!");
        int cardInd = Rand.getInstance().getRandomNumber(deck.getCards().size());
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);
        if (hand.size() == GameConfigs.maxCardInHand)
            return;
        hand.add(card);

    }

    public void playCard(Card baseCard) throws Exception {
        if (baseCard.getManaCost() > mana)
            throw new HearthStoneException("you don't have enough mana!");
        Card cardInHand = null;

        for (Card card : hand) {
            if (card.getName().equals(baseCard.getName())) {
                cardInHand = card;
            }
        }

        if (cardInHand instanceof HeroPowerCard) {
            if (heroPower != null) {
                throw new HearthStoneException("you are using hero power!");
            }
            heroPower = (HeroPowerCard) cardInHand;
        } else if (cardInHand instanceof SpellCard) {

        } else if (cardInHand instanceof RewardCard) {

        } else if (cardInHand instanceof WeaponCard) {
            if (weapon != null) {
                throw new HearthStoneException("you are using weapon!");
            }
            weapon = (WeaponCard) cardInHand;
        } else if (cardInHand instanceof MinionCard) {
            if (land.size() == GameConfigs.maxCardInLand)
                throw new HearthStoneException("your land is full!");
            land.add(cardInHand);
        }

        hand.remove(cardInHand);
        mana -= cardInHand.getManaCost();

        for (Card card : originalDeck.getCards()) {
            if (card.getName().equals(cardInHand.getName())) {
                originalDeck.cardPlay(cardInHand);
            }
        }
    }

    public void startTurn() throws Exception {
        mana = ++turnNumber;
        mana = Math.min(mana, GameConfigs.maxManaInGame);
        pickCard();
        /*if(passive.getName().equals("Twice Draw")){
            pickCard();
        }*/
    }
}
