package hearthstone.models.player;

import hearthstone.HearthStone;
import hearthstone.logic.GameConfigs;
import hearthstone.models.Deck;
import hearthstone.models.Passive;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;

import java.util.ArrayList;

public class Player {
    protected Hero hero;
    protected final Deck originalDeck;
    protected Deck deck;
    protected Passive passive;

    protected int mana;
    protected int turnNumber;

    protected boolean isStarted;
    protected Player enemyPlayer;

    private ArrayList<Card> waitingForDraw;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> land;

    protected HeroPowerCard heroPower;

    protected WeaponCard weapon;

    public Player(Hero hero, Deck deck) {
        this.hero = hero.copy();
        originalDeck = deck;
        this.deck = deck.copy();

        hand = new ArrayList<>();
        land = new ArrayList<>();
        waitingForDraw = new ArrayList<>();
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

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    // End of getter setter

    public void doPassives() {
        if (passive.getName().equals("Off Cards")) {
            for (Card card : deck.getCards()) {
                card.setManaCost(Math.max(0, card.getManaCost() - 1));
            }
        } else if (passive.getName().equals("Free Power")) {
            for (Card card : deck.getCards()) {
                if (card.getCardType() == CardType.HEROPOWER)
                    card.setManaCost(Math.max(0, card.getManaCost() - 1));
            }
        }
    }

    public void startGame() throws HearthStoneException {
        turnNumber = 0;
        mana = 0;

        pickCard();
        pickCard();
        pickCard();
    }

    public void pickCard() throws HearthStoneException {
        int cardInd = Rand.getInstance().getRandomNumber(deck.getCards().size());
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card card1 = waitingForDraw.get(i);
            if (card1.drawCard(card)) {
                waitingForDraw.remove(i);
                i--;
            }
        }

        hand.add(card);
    }

    public void pickCard(MinionType minionType) throws HearthStoneException {
        Card cardInHand = null;
        for (Card card : deck.getCards()) {
            if (card.getCardType() == CardType.MINIONCARD && ((MinionCard) card).getMinionType() == minionType) {
                deck.getCards().remove(card);
                cardInHand = card;
                break;
            }
        }

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        hand.add(cardInHand);
    }

    public void playCard(Card cardInHand) throws HearthStoneException {
        if (cardInHand.getManaCost() > mana)
            throw new HearthStoneException("you don't have enough mana!");

        switch (cardInHand.getCardType()) {
            case HEROPOWER:
                heroPower = (HeroPowerCard) cardInHand;
                break;
            case WEAPONCARD:
                weapon = (WeaponCard) cardInHand;
                break;
            case MINIONCARD:
                if (land.size() == GameConfigs.maxCardInLand)
                    throw new HearthStoneException("your land is full!");
                land.add(cardInHand);
                break;
            case HEROCARD:
            case SPELL:
            case REWARDCARD:
        }

        hand.remove(cardInHand);
        mana -= cardInHand.getManaCost();

        handleBattleCry(cardInHand);

        handlePlayCardOperationForDeck(cardInHand);

        handleWaitingCards(cardInHand);
    }

    private void handleBattleCry(Card card) {
        if (card.getCardType() == CardType.MINIONCARD) {
            ((MinionCard) card).battlecry();
        } else if (card.getCardType() == CardType.WEAPONCARD) {
            ((WeaponCard) card).battlecry();
        }
    }

    private void handleWaitingCards(Card cardInHand) {
        if (cardInHand.isWaitForDraw()) {
            waitingForDraw.add(cardInHand);
        }
    }

    private void handlePlayCardOperationForDeck(Card cardInHand) {
        for (Card card : originalDeck.getCards()) {
            if (card.getName().equals(cardInHand.getName())) {
                originalDeck.cardPlay(cardInHand);
            }
        }
    }

    public void summonMinionFromCurrentDeck(MinionType minionType) {
        if (originalDeck.getCards().size() == 0)
            return;
        int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
        for (int i = 0; i < deck.getCards().size(); i++) {
            Card card = deck.getCards().get(start);
            if (card.getCardType() == CardType.MINIONCARD &&
                    ((MinionCard)card).getMinionType() == minionType) {
                if (land.size() < GameConfigs.maxCardInLand) {
                    land.add(card);
                }
                return;
            }
            start++;
        }
    }

    public void makeAndPickCard(MinionType minionType) throws HearthStoneException {
        Card cardInHand = null;
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getCardType() == CardType.MINIONCARD && ((MinionCard) card).getMinionType() == minionType) {
                cardInHand = card.copy();
                configCard(cardInHand);
                break;
            }
        }

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        hand.add(cardInHand);
    }

    public void makeAndPlayCard(MinionType minionType) throws HearthStoneException {
        Card cardInLand = null;
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getCardType() == CardType.MINIONCARD && ((MinionCard) card).getMinionType() == minionType) {
                cardInLand = card.copy();
            }
        }
        if (land.size() == GameConfigs.maxCardInLand)
            throw new HearthStoneException("your land is full!");
        land.add(cardInLand);
    }

    public void makeAndPutDeck(Card card) {
        this.configCard(card);
        try {
            deck.addInTheMiddleOfGame(card);
        } catch (HearthStoneException ignore) {
        }
    }

    public Card getRandomCardFromOriginalDeck(CardType cardType) {
        if (originalDeck.getCards().size() == 0)
            return null;
        int start = Rand.getInstance().getRandomNumber(originalDeck.getCards().size());
        for (int i = 0; i < originalDeck.getCards().size(); i++) {
            Card card = originalDeck.getCards().get(start);
            if (card.getCardType() == cardType) {
                return card;
            }
            start++;
        }
        return null;
    }

    public Card getRandomCardFromCurrentDeck(CardType cardType) {
        if (originalDeck.getCards().size() == 0)
            return null;
        int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
        for (int i = 0; i < deck.getCards().size(); i++) {
            Card card = deck.getCards().get(start);
            if (card.getCardType() == cardType) {
                return card;
            }
            start++;
        }
        return null;
    }

    public void endTurn() {
        ArrayList<Card> cards = new ArrayList<>();
        for(Card card: land){
            cards.add(card);
        }

        for (Card card : cards) {
            MinionCard minionCard = (MinionCard) card;
            minionCard.endTurnBehave();
        }
    }

    public void startTurn() throws Exception {
        mana = ++turnNumber;
        mana = Math.min(mana, GameConfigs.maxManaInGame);
        pickCard();
        /*if(passive.getName().equals("Twice Draw")){
            pickCard();
        }*/

        for (Card card : land) {
            ((MinionCard) card).startTurnBehave();
        }
    }

    public void configCard(Card card) {
        card.setPlayer(this);
    }

    public void configHero(Hero hero) {
        hero.setPlayer(this);
    }

    public void updateCardsOnLand() {
        ArrayList<Card> deathRattles = new ArrayList<>();
        for (int i = 0; i < land.size(); i++) {
            Card card = land.get(i);
            if (((MinionCard) card).getHealth() <= 0) {
                if (((MinionCard) card).isDeathRattle()) {
                    deathRattles.add(card);
                }
                land.remove(card);
                i--;
            }
        }

        for (Card card : deathRattles) {
            ((MinionCard) card).deathRattle();
        }

        if (deathRattles.size() > 0) {
            enemyPlayer.updateCardsOnLand();
            this.updateCardsOnLand();
        }
    }

    public void updatePlayer() {
        updateCardsOnLand();
    }

    public boolean haveTaunt() {
        for (Card card : land) {
            if (((MinionCard) card).isTaunt())
                return true;
        }
        return false;
    }
}