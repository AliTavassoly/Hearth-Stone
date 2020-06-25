package hearthstone.logic.models.player;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.interfaces.*;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;
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

    private int manaSpentOnMinions, manaSpentOnSpells;

    protected boolean isStarted;
    protected int playerId;
    protected int enemyPlayerId;

    private ArrayList<Card> waitingForDraw;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> land;

    protected HeroPowerCard heroPower;
    protected RewardCard reward;
    protected WeaponCard weapon;

    public Player(Hero hero, Deck deck) {
        this.hero = hero.copy();
        originalDeck = deck;
        this.deck = deck.copy();

        hand = new ArrayList<>();
        land = new ArrayList<>();
        waitingForDraw = new ArrayList<>();

        configPlayer();
    }

    private void configPlayer() {
        for (Card card : deck.getCards()) {
            configCard(card);
        }
        configHero(hero);

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

    public void setEnemyPlayerId(int enemyPlayerId) {
        this.enemyPlayerId = enemyPlayerId;
    }

    public int getEnemyPlayerId() {
        return enemyPlayerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }


    public int getManaSpentOnMinions() {
        return manaSpentOnMinions;
    }

    public void setManaSpentOnMinions(int manaSpentOnMinions) {
        this.manaSpentOnMinions = manaSpentOnMinions;
    }

    public int getManaSpentOnSpells() {
        return manaSpentOnSpells;
    }

    public void setManaSpentOnSpells(int manaSpentOnSpells) {
        this.manaSpentOnSpells = manaSpentOnSpells;
    }

    public void setReward(RewardCard reward){
        this.reward = reward;
    }

    public RewardCard getReward(){
        return reward;
    }

/*    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }*/

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
        if (deck.getCards().size() == 0)
            throw new HearthStoneException("your deck is empty!");
        int cardInd = 0;
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card card1 = waitingForDraw.get(i);
            if (card1 instanceof WaitDrawingCard && ((WaitDrawingCard) card1).waitDrawingCard(card)) {
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
            case REWARDCARD:
                this.reward = (RewardCard) cardInHand;
                break;
            case SPELL:
        }

        hand.remove(cardInHand);
        mana -= cardInHand.getManaCost();

        handleBattleCry(cardInHand);

        handleCardPlayInformation(cardInHand);

        handleWaitingCards(cardInHand);

        Mapper.getInstance().updateBoard();
    }

    private void handleCardPlayInformation(Card card){
        if(card.getCardType() == CardType.SPELL)
            manaSpentOnSpells += card.getManaCost();
        if(card.getCardType() == CardType.MINIONCARD)
            manaSpentOnMinions += card.getManaCost();

        handlePlayCardOperationForDeck(card);
    }

    private void handleStartTurnBehaviours() {
        for (Card card : land) {
            ((MinionCard) card).startTurnBehave();
        }

        hero.startTurnBehave();

        if (heroPower != null) {
            // HERO POWER START TURN BEAHVE
        }

        if (weapon != null) {
            weapon.startTurnBehave();
        }
    }

    private void handleBattleCry(Card card) {
        if(card instanceof Battlecry)
            ((Battlecry)card).battlecry();
    }

    private void handleWaitingCards(Card cardInHand) {
        if (cardInHand instanceof WaitDrawingCard) {
            waitingForDraw.add(cardInHand);
        }
    }

    private void handleDeathRattles(ArrayList<Card> deathRattles) {
        for (Card card : deathRattles) {
            if (card instanceof DeathRattle) {
                ((DeathRattle) (card)).deathRattle();
            }
        }

        if (deathRattles.size() > 0) {
            Mapper.getInstance().updateBoard();
        }
    }

    private void handlePlayCardOperationForDeck(Card cardInHand) {
        for (Card card : originalDeck.getCards()) {
            if (card.getName().equals(cardInHand.getName())) {
                originalDeck.cardPlay(cardInHand);
            }
        }
    }

    private void handleFriendlyMinionDies(ArrayList<Card> friendlyMinionDies, int number) {
        for (Card card : friendlyMinionDies) {
            for (int i = 0; i < number; i++) {
                ((FriendlyMinionDies) card).friendlyMinionDies();
            }
        }
    }

    public void summonMinionFromCurrentDeck() {
        if (originalDeck.getCards().size() == 0)
            return;
        int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
        for (int i = 0; i < deck.getCards().size(); i++) {
            Card card = deck.getCards().get(start);
            if (card.getCardType() == CardType.MINIONCARD) {
                if (land.size() < GameConfigs.maxCardInLand) {
                    land.add(card);
                }
                return;
            }
            start++;
            start %= deck.getCards().size();
        }
    }

    public void summonMinionFromCurrentDeck(MinionType minionType) {
        if (originalDeck.getCards().size() == 0)
            return;
        int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
        for (int i = 0; i < deck.getCards().size(); i++) {
            Card card = deck.getCards().get(start);
            if (card.getCardType() == CardType.MINIONCARD &&
                    ((MinionCard) card).getMinionType() == minionType) {
                if (land.size() < GameConfigs.maxCardInLand) {
                    land.add(card);
                }
                return;
            }
            start++;
            start %= deck.getCards().size();
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

    public void makeAndSummonMinion(MinionType minionType) throws HearthStoneException {
        Card cardInLand = null;
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getCardType() == CardType.MINIONCARD && ((MinionCard) card).getMinionType() == minionType) {
                cardInLand = card.copy();
                configCard(cardInLand);
            }
        }
        if (land.size() == GameConfigs.maxCardInLand)
            throw new HearthStoneException("your land is full!");
        land.add(cardInLand);
    }

    public MinionCard getRandomMinionFromLand() {
        if (land.size() == 0)
            return null;
        int start = Rand.getInstance().getRandomNumber(land.size());
        for (int i = 0; i < land.size(); i++) {
            Card card = land.get(start);
            if (card.getCardType() == CardType.MINIONCARD) {
                return (MinionCard) card;
            }
            start++;
            start %= deck.getCards().size();
        }
        return null;
    }

    public void makeAndPutDeck(Card card) {
        this.configCard(card);
        try {
            deck.addInTheMiddleOfGame(card);
        } catch (HearthStoneException ignore) {
        }
    }

    public void makeAndSummonMinion(Card card) {
        if (land.size() < GameConfigs.maxCardInLand) {
            configCard(card);
            land.add(card);
        }
    }

    public void makeAndPutHand(Card card) {
        this.configCard(card);
        if (hand.size() < GameConfigs.maxCardInHand) {
            hand.add(card);
        }
    }

    public ArrayList<MinionCard> neighborCards(Card card) {
        ArrayList<MinionCard> ans = new ArrayList<>();
        for (int i = 0; i < land.size(); i++) {
            Card card1 = land.get(i);
            if (card == card1) {
                if (i + 1 < land.size())
                    ans.add((MinionCard) land.get(i + 1));
                if (i - 1 >= 0)
                    ans.add((MinionCard) land.get(i - 1));
                return ans;
            }
        }
        return ans;
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
            start %= deck.getCards().size();
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
            start %= deck.getCards().size();
        }
        return null;
    }

    public boolean haveTaunt() {
        for (Card card : land) {
            if (((MinionCard) card).isTaunt())
                return true;
        }
        return false;
    }

    public void endTurn() {
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : land) {
            cards.add(card);
        }

        for (Card card : cards) {
            MinionCard minionCard = (MinionCard) card;
            if (card instanceof EndTurnBehave)
                ((EndTurnBehave) minionCard).endTurnBehave();
        }

        Mapper.getInstance().updateBoard();
    }

    public void startTurn() throws Exception {
        //mana = ++turnNumber;
        mana = 10;
        mana = Math.min(mana, GameConfigs.maxManaInGame);
        pickCard();

        /*if(passive.getName().equals("Twice Draw")){
            pickCard();
        }*/

        handleStartTurnBehaviours();

        Mapper.getInstance().updateBoard();
    }

    private void configCard(Card card) {
        card.setPlayer(this);
    }

    private void configHero(Hero hero) {
        hero.setPlayer(this);
    }

    private void updateCardsOnLand() {
        int diedInThisMoment = 0;
        ArrayList<Card> deathRattles = new ArrayList<>();
        ArrayList<Card> friendlyMinionDies = new ArrayList<>();

        for (int i = 0; i < land.size(); i++) {
            Card card = land.get(i);

            if (((MinionCard) card).getHealth() > 0 && ((MinionCard) card) instanceof FriendlyMinionDies) {
                friendlyMinionDies.add(card);
            }

            if (((MinionCard) card).getHealth() <= 0) {

                if (((MinionCard) card).isDeathRattle()) {
                    deathRattles.add(card);
                }

                land.remove(card);
                diedInThisMoment++;
                i--;
            }
        }

        handleDeathRattles(deathRattles);

        handleFriendlyMinionDies(friendlyMinionDies, diedInThisMoment);
    }

    private void updateReward(){
        if(reward != null && reward.metCondition()){
            reward.doReward();
            reward = null;
        }
    }

    private void updateWeapon() {
        if (weapon != null && weapon.getDurability() < 0) {
            weapon = null;
        }
    }

    private void updateHeroPower() {
    }

    public void updatePlayer() {
        updateCardsOnLand();
        updateWeapon();
        updateCardsOnLand();
        updateReward();
    }
}