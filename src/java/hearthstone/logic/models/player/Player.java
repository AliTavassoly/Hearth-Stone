package hearthstone.logic.models.player;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.interfaces.*;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.passive.Passive;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
    protected Hero hero;
    protected final Deck originalDeck;
    protected Deck deck;
    protected Passive passive;

    private String username;

    protected int mana, extraMana;
    protected int turnNumber;

    private int manaSpentOnMinions, manaSpentOnSpells;

    protected boolean isStarted;
    protected int playerId;

    private ArrayList<Card> waitingForDraw;
    private ArrayList<Card> waitingForSummon;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> land;

    protected HeroPowerCard heroPower;
    protected RewardCard reward;
    protected WeaponCard weapon;

    private final CardFactory factory;

    public Player(Hero hero, Deck deck, String username) {
        this.hero = hero.copy();
        originalDeck = deck;
        this.deck = deck.copy();

        this.username = username;

        hand = new ArrayList<>();
        land = new ArrayList<>();
        waitingForDraw = new ArrayList<>();
        waitingForSummon = new ArrayList<>();
        factory = new CardFactory();
    }

    public void configPlayer(boolean shuffle) {
        for (Card card : deck.getCards()) {
            configCard(card);
        }

        configPassive(passive);

        configHero(hero);

        if (shuffle)
            Collections.shuffle(deck.getCards());

        mana = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setReward(RewardCard reward) {
        this.reward = reward;
    }

    public RewardCard getReward() {
        return reward;
    }

    public CardFactory getFactory() {
        return factory;
    }

    public boolean haveWeapon() {
        return weapon != null;
    }

    public void setExtraMana(int extraMana){
        this.extraMana = extraMana;
    }
    // End of getter setter

    public void reduceMana(int reduce) {
        mana -= reduce;
    }

    public void startGame() throws HearthStoneException {
        turnNumber = 0;
        mana = 0;

        handleStartGameBehave();

        drawCard();
        drawCard();
        drawCard();
    }

    // CARD ACTION
    public void drawCard() throws HearthStoneException {
        if (deck.getCards().size() == 0)
            throw new HearthStoneException("your deck is empty!");
        int cardInd = 0;
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card waitedCard = waitingForDraw.get(i);
            if (((WaitDrawingCard) waitedCard).waitDrawingCard(card)) {
                waitingForDraw.remove(i);
                i--;
            }
        }

        hand.add(card);
    }

    public void drawCard(int ind) throws HearthStoneException {
        if (deck.getCards().size() == 0)
            throw new HearthStoneException("your deck is empty!");
        int cardInd = ind;
        Card card = deck.getCards().get(cardInd);
        deck.getCards().remove(cardInd);

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card waitedCard = waitingForDraw.get(i);
            if (((WaitDrawingCard) waitedCard).waitDrawingCard(card)) {
                waitingForDraw.remove(i);
                i--;
            }
        }

        hand.add(card);
    }

    public void drawCard(Card card) throws HearthStoneException {
        if (deck.getCards().size() == 0)
            throw new HearthStoneException("your deck is empty!");
        deck.getCards().remove(card);

        if (hand.size() == GameConfigs.maxCardInHand)
            throw new HearthStoneException("your hand is full!");

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card waitedCard = waitingForDraw.get(i);
            if (((WaitDrawingCard) waitedCard).waitDrawingCard(card)) {
                waitingForDraw.remove(i);
                i--;
            }
        }

        hand.add(card);
    }

    public void discardCard(Card cardInHand) throws HearthStoneException {
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
                break;
            case REWARDCARD:
                this.reward = (RewardCard) cardInHand;
                break;
            case SPELL:
                break;
        }

        hand.remove(cardInHand);

        handleBattleCry(cardInHand);

        handleCardPlayInformation(cardInHand);

        handleWaitingDrawCards(cardInHand);

        if (cardInHand.getCardType() == CardType.MINIONCARD)
            summonMinion(cardInHand);

        if (cardInHand.getCardType() == CardType.SPELL)
            playSpell(cardInHand);

        Mapper.getInstance().updateBoard();
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
                break;
            case REWARDCARD:
                this.reward = (RewardCard) cardInHand;
                break;
            case SPELL:
                break;
        }

        hand.remove(cardInHand);
        mana -= cardInHand.getManaCost();

        handleBattleCry(cardInHand);

        handleCardPlayInformation(cardInHand);

        handleWaitingDrawCards(cardInHand);

        if (cardInHand.getCardType() == CardType.MINIONCARD)
            summonMinion(cardInHand);

        if (cardInHand.getCardType() == CardType.SPELL)
            playSpell(cardInHand);

        Mapper.getInstance().updateBoard();
    }

    private void summonMinion(Card card) {
        MinionCard minionCard = (MinionCard) card;

        handleWaitingSummon(card);

        minionCard.setInitialAttack(minionCard.getAttack());
        minionCard.setInitialHealth(minionCard.getHealth());

        if (card instanceof WaitSummonCard)
            waitingForSummon.add(card);

        land.add(minionCard);
    }

    private void playSpell(Card card) {
        SpellCard spell = (SpellCard) card;

        Mapper.getInstance().animateSpell(getPlayerId(), card);

        spell.doAbility();
    }
    // CARD ACTION

    // HANDLE INTERFACES
    private void handleStartGameBehave(){
        for(Card card: deck.getCards()){
            if(card instanceof StartGameBehave){
                ((StartGameBehave)card).startGameBehave();;
            }
        }

        if(hero instanceof StartGameBehave){
            ((StartGameBehave)hero).startGameBehave();
        }

        if(hero.getSpecialHeroPower() instanceof StartGameBehave){
            ((StartGameBehave)hero.getSpecialHeroPower()).startGameBehave();
        }

        if(heroPower instanceof StartGameBehave){
            ((StartGameBehave)heroPower).startGameBehave();
        }

        if(passive instanceof StartGameBehave){
            ((StartGameBehave)passive).startGameBehave();
        }
    }

    private void handleStartTurnBehaviours() {
        for (Card card : land) {
            ((MinionCard) card).startTurnBehave();
        }

        hero.startTurnBehave();

        if (heroPower != null) {
            heroPower.startTurnBehave();
        }

        updateWeapon();
        if (weapon != null) {
            weapon.startTurnBehave();
        }

        if (passive instanceof StartTurnBehave) {
            ((StartTurnBehave) passive).startTurnBehave();
        }
    }

    private void handleCardPlayInformation(Card card) {
        if (card.getCardType() == CardType.SPELL)
            manaSpentOnSpells += card.getManaCost();
        if (card.getCardType() == CardType.MINIONCARD)
            manaSpentOnMinions += card.getManaCost();

        handlePlayCardOperationForDeck(card);
    }

    private void handleEndTurnBehaviours(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(land);

        for (Card card : cards) {
            MinionCard minionCard = (MinionCard) card;
            if (card instanceof EndTurnBehave)
                ((EndTurnBehave) minionCard).endTurnBehave();
        }

        if(passive instanceof EndTurnBehave){
            ((EndTurnBehave)passive).endTurnBehave();
        }

        if(hero.getSpecialHeroPower() instanceof EndTurnBehave){
            ((EndTurnBehave)hero.getSpecialHeroPower()).endTurnBehave();
        }
    }

    private void handleBattleCry(Card card) {
        if (card instanceof Battlecry)
            ((Battlecry) card).battlecry();
    }

    private void handleWaitingDrawCards(Card cardInHand) {
        if (cardInHand instanceof WaitDrawingCard) {
            waitingForDraw.add(cardInHand);
        }
    }

    private void handleWaitingSummon(Card cardInHand) {
        for (int i = 0; i < waitingForSummon.size(); i++) {
            Card card1 = waitingForSummon.get(i);
            try {
                if (card1 instanceof WaitSummonCard && ((WaitSummonCard) card1).waitSummonCard(cardInHand) && cardInHand != card1) {
                    waitingForSummon.remove(i);
                    i--;
                }
            } catch (HearthStoneException e) {
            }
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

    private void handleFriendlyMinionDies(ArrayList<Card> friendlyMinionDies, int number) {
        for (Card card : friendlyMinionDies) {
            for (int i = 0; i < number; i++) {
                ((FriendlyMinionDies) card).friendlyMinionDies();
            }
        }
    }

    private void handlePlayCardOperationForDeck(Card cardInHand) {
        for (Card card : originalDeck.getCards()) {
            if (card.getName().equals(cardInHand.getName())) {
                originalDeck.cardPlay(cardInHand);
            }
        }
    }
    // HANDLE INTERFACES

    public void discountSpells(int mana){
        for(Card card: deck.getCards()){
            if(card.getCardType() == CardType.SPELL){
                Mapper.getInstance().setCardMana(card, Math.max(0, card.getManaCost() - mana));
            }
        }
    }

    public void discountAllDeckCard(int mana){
        for(Card card: deck.getCards()){
            card.setManaCost(Math.max(0, card.getManaCost() - mana));
        }
    }

    public boolean haveTaunt() {
        for (Card card : land) {
            if (((MinionCard) card).isTaunt())
                return true;
        }
        return false;
    }

    public void endTurn() {
        handleEndTurnBehaviours();

        Mapper.getInstance().updateBoard();
    }

    public void startTurn() throws Exception {
        mana = ++turnNumber + extraMana;
        //mana = 10;
        mana = Math.min(mana, GameConfigs.maxManaInGame);

        /*if(passive.getName().equals("Twice Draw")){
            pickCard();
        }*/

        handleStartTurnBehaviours();

        Mapper.getInstance().updateBoard();

        drawCard();

        Mapper.getInstance().updateBoard();
    }

    public void configPassive(Passive passive){
        passive.setPlayerId(getPlayerId());
    }

    private void configCard(Card card) {
        card.setPlayerId(this.getPlayerId());
    }

    private void configHero(Hero hero) {
        hero.setPlayerId(this.getPlayerId());

        HeroPowerCard heroPower = (HeroPowerCard) HearthStone.getCardByName(hero.getHeroPowerName());
        configCard(heroPower);
        this.heroPower = heroPower;

        hero.getSpecialHeroPower().setPlayerId(getPlayerId());
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

    private void updateReward() {
        if (reward != null && reward.metCondition()) {
            reward.doReward();
            reward = null;
        }
    }

    private void updateWeapon() {
        if (weapon != null && weapon.getDurability() < 0) {
            Mapper.getInstance().setWeapon(getPlayerId(), null);
        }
    }

    public void lostGame() {
        originalDeck.setTotalGames(originalDeck.getTotalGames() + 1);
    }

    public void wonGame() {
        originalDeck.setTotalGames(originalDeck.getWinGames() + 1);
        originalDeck.setWinGames(originalDeck.getWinGames() + 1);
    }

    public ArrayList<Card> getTopCards(int numberOfTopCards) {
        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < Math.min(numberOfTopCards, deck.getCards().size()); i++) {
            cards.add(deck.getCards().get(i));
        }

        return cards;
    }

    public void removeInitialCards(ArrayList<Card> discardCards, int numberOfTopCards) {
        for (int i = 0; i < numberOfTopCards; i++) {
            Card card = deck.getCards().get(i);
            if (discardCards.contains(card)) {
                deck.getCards().remove(i);

                i--;

                deck.getCards().add(card);
            }
        }
    }

    public void updatePlayer() {
        updateCardsOnLand();
        updateWeapon();
        updateCardsOnLand();
        updateReward();
    }

    public void transformMinion(MinionCard oldMinion, MinionCard newMinion) {
        for (int i = 0; i < land.size(); i++) {
            Card card = land.get(i);
            if (card == oldMinion) {
                land.set(i, newMinion);
                break;
            }
        }

        for (int i = 0; i < waitingForDraw.size(); i++) {
            Card card = waitingForDraw.get(i);
            if (card == oldMinion) {
                waitingForDraw.remove(i);
                break;
            }
        }

        for (int i = 0; i < waitingForSummon.size(); i++) {
            Card card = waitingForSummon.get(i);
            if (card == oldMinion) {
                waitingForSummon.remove(i);
                break;
            }
        }

        configCard(newMinion);
    }

    public class CardFactory {
        public CardFactory() {
        }

        public void summonMinionFromCurrentDeck() {
            if (deck.getCards().size() == 0 || land.size() == GameConfigs.maxCardInLand)
                return;
            int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
            for (int i = 0; i < deck.getCards().size(); i++) {
                Card card = deck.getCards().get(start);
                if (card.getCardType() == CardType.MINIONCARD) {
                    summonMinion(card);
                    deck.getCards().remove(start);
                    return;
                }
                start++;
                start %= deck.getCards().size();
            }
        }

        public void summonMinionFromCurrentDeck(int attack, int health) {
            if (deck.getCards().size() == 0 || land.size() == GameConfigs.maxCardInLand)
                return;
            int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
            for (int i = 0; i < deck.getCards().size(); i++) {
                Card card = deck.getCards().get(start);
                if (card.getCardType() == CardType.MINIONCARD) {
                    MinionCard minion = (MinionCard) card;
                    if (minion.getAttack() == attack && minion.getHealth() == health) {
                        summonMinion(card);
                        deck.getCards().remove(start);
                        return;
                    }
                }
                start++;
                start %= deck.getCards().size();
            }
        }

        public void summonMinionFromCurrentDeck(MinionType minionType) {
            if (deck.getCards().size() == 0 || land.size() == GameConfigs.maxCardInLand)
                return;

            int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
            for (int i = 0; i < deck.getCards().size(); i++) {
                Card card = deck.getCards().get(start);
                if (card.getCardType() == CardType.MINIONCARD &&
                        ((MinionCard) card).getMinionType() == minionType) {
                    summonMinion(card);
                    deck.getCards().remove(start);
                    return;
                }
                start++;
                start %= deck.getCards().size();
            }
        }

        public void summonMinionFromCurrentDeck(String cardName) {
            if (deck.getCards().size() == 0 || land.size() == GameConfigs.maxCardInLand)
                return;

            int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
            for (int i = 0; i < deck.getCards().size(); i++) {
                Card card = deck.getCards().get(start);
                if (card.getCardType() == CardType.MINIONCARD &&
                        card.getName().equals(cardName)) {
                    summonMinion(card);
                    deck.getCards().remove(start);
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
            summonMinion(cardInLand);
        }

        public MinionCard getRandomMinionFromLand() {
            if (land.size() == 0)
                return null;
            int randId = Rand.getInstance().getRandomNumber(land.size());
            return (MinionCard) land.get(randId);
        }

        public void makeAndPutDeck(Card card) {
            configCard(card);
            try {
                deck.addInTheMiddleOfGame(card);
            } catch (HearthStoneException ignore) {
            }
        }

        public void makeAndSummonMinion(Card card) {
            if (land.size() < GameConfigs.maxCardInLand) {
                configCard(card);
                summonMinion(card);
            }
        }

        public void makeAndPutHand(Card card) {
            configCard(card);
            if (hand.size() < GameConfigs.maxCardInHand) {
                hand.add(card);
            }
        }

        public Card getRandomCardFromHand() {
            if (hand.size() == 0)
                return null;
            return hand.get(Rand.getInstance().getRandomNumber(hand.size()));
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

        public Card getRandomCardFromCurrentDeck() {
            if (deck.getCards().size() == 0)
                return null;
            int start = Rand.getInstance().getRandomNumber(deck.getCards().size());
            return deck.getCards().get(start);
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

        public void removeFromDeck(Card card) {
            deck.getCards().remove(card);
        }

        public void removeFromHand(Card card) {
            hand.remove(card);
        }
    }
}