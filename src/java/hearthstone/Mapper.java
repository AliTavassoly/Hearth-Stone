package hearthstone;

import hearthstone.data.DataBase;
import hearthstone.client.gui.game.play.boards.GameBoard;
import hearthstone.logic.Game;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.models.player.AIPlayer;
import hearthstone.models.player.Player;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Mapper {
    public static Player getPlayer(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId);
    }

    public static GameBoard getGameBoard() {
        return HearthStone.currentGameBoard;
    }

    public static Game getGame() {
        return HearthStone.currentGame;
    }

    public static void playCard(int playerId, Card card) throws Exception {
        getPlayer(playerId).playCard(card);
    }

    public static void endTurn() {
        getGame().endTurn();
    }

    public static void startGame() {
        getGame().startGame();
    }

    public static void gameEnded() {
        getGameBoard().gameEnded();
    }

    public static void lost(int playerId) {
        getPlayer(playerId).lostGame();
    }

    public static void won(int playerId) {
        getPlayer(playerId).wonGame();
    }

    public static void setPassive(int playerId, Passive passive) {
        getPlayer(playerId).setPassive(passive);
    }

    public static void foundObjectForObject(Object waited, Object founded) throws HearthStoneException {
        if (waited instanceof Card) {
            Card card = (Card) waited;
            card.found(founded);
        }
    }

    public static void deleteCurrentMouseWaiting() {
        getGameBoard().deleteCurrentMouseWaiting();
    }

    public static void makeNewMouseWaiting(CursorType cursorType, Card card) {
        getGameBoard().makeNewMouseWaiting(cursorType, card);
    }

    public static void removeInitialCards(int playerId, ArrayList<Card> discardCards, int numberOfTopCards) {
        getPlayer(playerId).removeInitialCards(discardCards, numberOfTopCards);
    }

    public static void passPassivesToAI(int playerId, ArrayList<Integer> passives) {
        ((AIPlayer) getPlayer(playerId)).choosePassive(passives);
    }

    public static void animateSpell(int playerId, Card card) { // could not remove card object
        getGameBoard().animateSpell(playerId, card);
    }

    public static void buyCard(int cardId) throws Exception {
        Card card = HearthStone.getCardById(cardId);
        HearthStone.currentAccount.buyCards(card, 1);
        HearthStone.market.removeCard(card, 1);
    }

    public static void sellCard(int cardId) throws Exception {
        Card card = HearthStone.getCardById(cardId);
        HearthStone.currentAccount.sellCards(card, 1);
        HearthStone.market.addCard(card.copy(), 1);
    }

    public static void saveDataBase() throws Exception {
        DataBase.save();
    }

    public static void updateBoard() {
        getPlayer(0).updatePlayer();
        getPlayer(1).updatePlayer();
        getGameBoard().restart();
    }

    public static void removeFromDeck(Deck deck, Card card, int cnt) throws Exception{
        deck.remove(card, cnt);
    }

    public static void addToDeck(Deck deck, Card card, int cnt) throws Exception{
        deck.add(card, cnt);
    }

    public static int getEnemyId(int playerId){
        if(playerId == 0)
            return 1;
        return 0;
    }

    public static boolean isLost(int playerId){
        return getPlayer(playerId).getHero().getHealth() <= 0;
    }

    public static ArrayList<Card> getTopCards(int playerId, int numberOfTopCards){
        return getPlayer(playerId).getTopCards(numberOfTopCards);
    }

    public static String getPlayerName(int playerId){
        return getPlayer(playerId).getUsername();
    }

    public static Hero getHero(int playerId) {
        return getPlayer(playerId).getHero();
    }

    public static RewardCard getReward(int playerId) {
        return getPlayer(playerId).getReward();
    }

    public static WeaponCard getWeapon(int playerId) {
        return getPlayer(playerId).getWeapon();
    }

    public static HeroPowerCard getHeroPower(int playerId) {
        return getPlayer(playerId).getHeroPower();
    }

    public static Passive getPassive(int playerId) {
        return getPlayer(playerId).getPassive();
    }

    public static int getTurnNumber(int playerId) {
        return getPlayer(playerId).getTurnNumber();
    }

    public static int getMana(int playerId) {
        return getPlayer(playerId).getMana();
    }

    public static int getDeckSize(int playerId) {
        return getPlayer(playerId).getDeck().getCards().size();
    }

    public static ArrayList<Card> getLand(int playerId) {
        return getPlayer(playerId).getLand();
    }

    public static ArrayList<Card> getHand(int playerId) {
        return getPlayer(playerId).getHand();
    }

    public static int getWhoseTurn() {
        return getGame().getWhoseTurn();
    }
}
