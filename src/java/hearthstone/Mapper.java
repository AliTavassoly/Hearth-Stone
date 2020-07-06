package hearthstone;

import hearthstone.gui.game.play.boards.GameBoard;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.Character;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.passives.Passive;
import hearthstone.logic.models.player.AIPlayer;
import hearthstone.logic.models.player.Player;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Mapper {
    private static Mapper instance;

    private Mapper() {
    }

    public static Mapper getInstance() {
        if (instance == null) {
            return instance = new Mapper();
        } else {
            return instance;
        }
    }

    public Player getPlayer(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId);
    }

    public GameBoard getGameBoard() {
        return HearthStone.currentGameBoard;
    }

    public void setExtraMane(int playerId, int extraMana){
        getPlayer(playerId).setExtraMana(extraMana);
    }

    public Game getGame() {
        return HearthStone.currentGame;
    }

    public void playCard(int playerId, Card card) throws Exception {
        getPlayer(playerId).playCard(card);
    }

    public void endTurn() {
        getGame().endTurn();
    }

    public void startGame() {
        getGame().startGame();
    }

    public void startGame(int playerId) throws HearthStoneException{
        getPlayer(playerId).startGame();
    }

    public void startTurn(int playerId) throws Exception{
        getPlayer(playerId).startTurn();
    }

    public void endTurn(int playerId){
        getPlayer(playerId).endTurn();
    }

    public void gameEnded() {
        getGameBoard().gameEnded();
    }

    public void lost(int playerId) {
        getPlayer(playerId).lostGame();
    }

    public void won(int playerId) {
        getPlayer(playerId).wonGame();
    }

    public void addAttack(int attack, MinionCard minionCard) {
        minionCard.changeAttack(attack);
        updateBoard();
    }

    public void addAttack(int attack, WeaponCard weaponCard) {
        weaponCard.setAttack(weaponCard.getAttack() + attack);
        updateBoard();
    }

    public void addDurability(int durability, WeaponCard weaponCard) {
        weaponCard.setDurability(weaponCard.getDurability() + durability);
        updateBoard();
    }

    public void addHealth(int health, MinionCard minionCard) {
        minionCard.gotHeal(health);
        updateBoard();
    }

    public void setHealth(int health, Character character) {
        character.setHealth(health);
        updateBoard();
    }

    public void damage(int damage, Character character) throws HearthStoneException {
        character.gotDamage(damage);
        updateBoard();
    }

    public void damage(int damage, Character character, boolean update) throws HearthStoneException {
        character.gotDamage(damage);
        if (update)
            updateBoard();
    }

    public void heal(int heal, Character character) {
        character.gotHeal(heal);
    }

    public void restoreHealth(Character character) {
        character.restoreHealth();
    }

    public void restoreHealth(int heal, Character character) {
        character.restoreHealth(heal);
    }

    public void setPassive(int playerId, Passive passive) {
        getPlayer(playerId).setPassive(passive);
    }

    public void restartSpentManaOnMinions(int playerId) {
        getPlayer(playerId).setManaSpentOnMinions(0);
    }

    public void restartSpentManaOnSpells(int playerId) {
        getPlayer(playerId).setManaSpentOnSpells(0);
    }

    public void foundObjectForObject(Object waited, Object founded) throws HearthStoneException {
        if (waited instanceof Card) {
            Card card = (Card) waited;
            card.found(founded);
        }
    }

    public void deleteCurrentMouseWaiting() {
        getGameBoard().deleteCurrentMouseWaiting();
    }

    public void makeNewMouseWaiting(CursorType cursorType, Card card) {
        getGameBoard().makeNewMouseWaiting(cursorType, card);
    }

    public void summonMinionFromCurrentDeck(int playerId, int attack, int health){
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(attack, health);
    }

    public void summonMinionFromCurrentDeck(int playerId, MinionType minionType){
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(minionType);
    }

    public void summonMinionFromCurrentDeck(int playerId){
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck();
    }

    public void summonMinionFromCurrentDeck(int playerId, String cardName){
        getPlayer(playerId).getFactory().summonMinionFromCurrentDeck(cardName);
    }

    public void makeAndPutHand(int playerId, Card card){
        getPlayer(playerId).getFactory().makeAndPutHand(card);
    }

    public void isAttacked(IsAttacked character){
        character.isAttacked();
    }

    public void removeInitialCards(int playerId, ArrayList<Card> discardCards, int numberOfTopCards) {
        getPlayer(playerId).removeInitialCards(discardCards, numberOfTopCards);
    }

    public void setPlayerMana(int playerId, int mana) {
        getPlayer(playerId).setMana(mana);
    }

    public void reduceMana(int playerId, int reduce) {
        getPlayer(playerId).reduceMana(reduce);
    }

    public void makeAndSummonMinion(int playerId, Card card) {
        getPlayer(playerId).getFactory().makeAndSummonMinion(card);
    }

    public void makeAndPutDeck(int playerId, Card card) {
        getPlayer(playerId).getFactory().makeAndPutDeck(card);
    }

    public void setHeroImmune(int playerId, boolean immune){
        getPlayer(playerId).getHero().setImmune(immune);
    }

    public void drawCard(int playerId, int ind) throws HearthStoneException{
        getPlayer(playerId).drawCard(ind);
    }

    public void drawCard(int playerId) throws HearthStoneException{
        getPlayer(playerId).drawCard();
    }

    public void drawCard(int playerId, Card card) throws HearthStoneException{
        getPlayer(playerId).drawCard(card);
        updateBoard();
    }

    public void handleImmunities(int playerId, Character character){
        character.handleImmunities();
    }

    public void handleFreezes(int playerId, Character character){
        character.handleFreezes();
    }

    public void addFreezes(int playerId, int turnNumber, Character character){
        character.addFreezes(turnNumber);
        handleFreezes(playerId, character);
    }

    public void addImmunity(int playerId, int turnNumber, Character character){
        character.addImmunity(turnNumber);
        handleImmunities(playerId, character);
    }

    public void reduceFreezes(int playerId, Character character){
        character.reduceFreezes();
    }

    public void reduceImmunities(int playerId, Character character){
        character.reduceImmunities();
    }

    public void setTaunt(boolean isTaunt, MinionCard minionCard){
        minionCard.setTaunt(isTaunt);
    }

    public void setCharge(boolean isCharge, MinionCard minionCard) {
        minionCard.setCharge(isCharge);
    }

    public void setDivineShield(boolean isDivineShields, MinionCard minionCard){
        minionCard.setDivineShield(isDivineShields);
    }

    public void removeDivineShield(MinionCard minionCard){

    }

    public void setCardMana(Card card, int mana){
        card.setManaCost(mana);
    }

    public void setHeroPowerExtraAttack(HeroPowerCard power, int extra){
        power.setExtraNumberOfAttack(extra);
    }

    public void discountAllDeckCard(int playerId, int mana){
        getPlayer(playerId).discountAllDeckCard(mana);
    }

    public void transformMinion(int playerId, MinionCard oldMinion, MinionCard newMinion){
        getPlayer(playerId).transformMinion(oldMinion, newMinion);
    }

    public void passPassivesToAI(int playerId, ArrayList<Integer> passives){
        ((AIPlayer)getPlayer(playerId)).choosePassive(passives);
    }

    public void setSpellSafe(MinionCard minionCard, boolean isSpellSafe){
        minionCard.setSpellSafe(isSpellSafe);
    }

    public void setHeroPower(int playerId, HeroPowerCard heroPower){
        getPlayer(playerId).setHeroPower(heroPower);
    }

    public void discardCard(int playerId, Card card) throws HearthStoneException{
        getPlayer(playerId).discardCard(card);
    }

    public void animateSpell(int playerId, Card card){
        getGameBoard().animateSpell(playerId, card);
    }

    public void stealFromDeck(int stealerId, int stolenId, Card card){
        getPlayer(stolenId).getFactory().removeFromDeck(card);
        getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardByName(card.getName()));
    }

    public void stealFromHand(int stealerId, int stolenId, Card card){
        getPlayer(stolenId).getFactory().removeFromHand(card);
        getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardByName(card.getName()));
    }

    public void updateBoard() {
        getPlayer(0).updatePlayer();
        getPlayer(1).updatePlayer();
        getGameBoard().restart();
    }
}
