package hearthstone;

import hearthstone.data.DataBase;
import hearthstone.gui.game.play.boards.GameBoard;
import hearthstone.logic.gamestuff.Game;
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
    /*public static void startGame(int playerId) throws HearthStoneException {
        getPlayer(playerId).startGame();
    }*/

    /*public static void startTurn(int playerId) throws Exception {
        getPlayer(playerId).startTurn();
    }*/

    /*public static void endTurn(int playerId) {
        getPlayer(playerId).endTurn();
    }*/

    /*public static void setExtraMane(int playerId, int extraMana) {
        getPlayer(playerId).setExtraMana(extraMana);
    }*/

    /*public static void addAttack(int attack, MinionCard minionCard) {
        minionCard.changeAttack(attack);
        updateBoard();
    }*/

    /*public static void addAttack(int attack, WeaponCard weaponCard) {
        weaponCard.setAttack(weaponCard.getAttack() + attack);
        updateBoard();
    }*/

    /*public static void addDurability(int durability, WeaponCard weaponCard) {
        weaponCard.setDurability(weaponCard.getDurability() + durability);
        updateBoard();
    }*/

    /*public static void addHealth(int health, MinionCard minionCard) {
        minionCard.gotHeal(health);
        updateBoard();
    }*/

    /*public static void setHealth(int health, Character character) {
        character.setHealth(health);
        updateBoard();
    }*/

    /*public static void damage(int damage, Character character) throws HearthStoneException {
        character.gotDamage(damage);
        updateBoard();
    }*/

    /*public static void damage(int damage, Character character, boolean update) throws HearthStoneException {
        character.gotDamage(damage);
        if (update)
            updateBoard();
    }*/

    /*public static void heal(int heal, Character character) {
        character.gotHeal(heal);
    }*/

    /*public static void restoreHealth(Character character) {
        character.restoreHealth();
    }*/

    /*public static void restoreHealth(int heal, Character character) {
        character.restoreHealth(heal);
    }*/

    /*public static void restartSpentManaOnMinions(int playerId) {
        getPlayer(playerId).setManaSpentOnMinions(0);
    }*/

    /*public static void restartSpentManaOnSpells(int playerId) {
        getPlayer(playerId).setManaSpentOnSpells(0);
    }*/

    /*public static void summonMinionFromCurrentDeck(int playerId, int attack, int health) {
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(attack, health);
    }*/

    /*public static void summonMinionFromCurrentDeck(int playerId, MinionType minionType) {
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(minionType);
    }*/

    /*public static void summonMinionFromCurrentDeck(int playerId) {
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck();
    }*/

    /*public static void summonMinionFromCurrentDeck(int playerId, String cardName) {
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(cardName);
    }*/

    /*public static void makeAndPutHand(int playerId, Card card) {
        getPlayer(playerId).getFactory().makeAndPutHand(card);
    }*/

    /*public static void isAttacked(IsAttacked character) {
        character.isAttacked();
    }*/

    /*public static void setPlayerMana(int playerId, int mana) {
        getPlayer(playerId).setMana(mana);
    }*/

    /*public static void reduceMana(int playerId, int reduce) {
        getPlayer(playerId).reduceMana(reduce);
    }*/

    /*public static void makeAndSummonMinion(int playerId, Card card) {
        getPlayer(playerId).getFactory().makeAndSummonMinion(card);
    }*/

    /*public static void makeAndPutDeck(int playerId, Card card) {
        getPlayer(playerId).getFactory().makeAndPutDeck(card);
    }*/

    /*public static void setHeroImmune(int playerId, boolean immune) {
        getPlayer(playerId).getHero().setImmune(immune);
    }*/

    /*public static void drawCard(int playerId, int ind) throws HearthStoneException {
        getPlayer(playerId).drawCard(ind);
    }*/

    /*public static void drawCard(int playerId) throws HearthStoneException {
        getPlayer(playerId).drawCard();
    }*/

    /*public static void drawCard(int playerId, Card card) throws HearthStoneException {
        getPlayer(playerId).drawCard(card);
        updateBoard();
    }*/

    /*public static void handleImmunities(int playerId, Character character) {
        character.handleImmunities();
    }*/

    /*public static void handleFreezes(int playerId, Character character) {
        character.handleFreezes();
    }*/

    /*public static void addFreezes(int playerId, int turnNumber, Character character) {
        character.addFreezes(turnNumber);
        handleFreezes(playerId, character);
    }*/

    /*public static void addImmunity(int playerId, int turnNumber, Character character) {
        character.addImmunity(turnNumber);
        handleImmunities(playerId, character);
    }*/

    /*public static void reduceFreezes(int playerId, Character character) {
        character.reduceFreezes();
    }*/

    /*public static void reduceImmunities(int playerId, Character character) {
        character.reduceImmunities();
    }*/

    /*public static void setTaunt(boolean isTaunt, MinionCard minionCard) {
        minionCard.setTaunt(isTaunt);
    }*/

    /*public static void setWeapon(int playerId, WeaponCard weaponCard) {
        getPlayer(playerId).setWeapon(weaponCard);
    }*/

    /*public static void setCharge(boolean isCharge, MinionCard minionCard) {
        minionCard.setCharge(isCharge);
    }*/

    /*public static void setDivineShield(boolean isDivineShields, MinionCard minionCard) {
        minionCard.setDivineShield(isDivineShields);
    }*/

    /*public static void removeDivineShield(MinionCard minionCard) {

    }*/

    /*public static void setCardMana(Card card, int mana) {
        card.setManaCost(mana);
    }*/

    /*public static void setHeroPowerExtraAttack(HeroPowerCard power, int extra) {
        power.setExtraNumberOfAttack(extra);
    }*/

    /*public static void discountAllDeckCard(int playerId, int mana) {
        getPlayer(playerId).discountAllDeckCard(mana);
    }*/

    /*public static void discountSpells(int playerId, int mana) {
        getPlayer(playerId).discountSpells(mana);
    }*/

    /*public static void transformMinion(int playerId, int oldMinionId, MinionCard newMinion) {
        getPlayer(playerId).transformMinion(oldMinionId, newMinion);
    }*/

    /*public static void setSpellSafe(MinionCard minionCard, boolean isSpellSafe) {
        minionCard.setSpellSafe(isSpellSafe);
    }*/

    /*public static void setHeroPower(int playerId, HeroPowerCard heroPower) {
        getPlayer(playerId).setHeroPower(heroPower);
    }*/

    /*public static void discardCard(int playerId, int cardId) throws HearthStoneException {
        getPlayer(playerId).discardCard(cardId);
    }*/

    /*public static void stealFromDeck(int stealerId, int stolenId, int cardId) {
        getPlayer(stolenId).getFactory().removeFromDeck(cardId);
        getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardById(cardId));
    }*/

    /*public static void stealFromHand(int stealerId, int stolenId, int cardId) {
        getPlayer(stolenId).getFactory().removeFromHand(cardId);
        getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardById(cardId));
    }*/

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
