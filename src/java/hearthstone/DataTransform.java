package hearthstone;

import hearthstone.gui.game.play.boards.GameBoard;
import hearthstone.logic.GameConfigs;
import hearthstone.models.behaviours.Character;
import hearthstone.logic.gamestuff.Game;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.models.player.Player;

import java.util.ArrayList;

public class DataTransform {
    public static Player getPlayer(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId);
    }

    public static Game getGame(){
        return HearthStone.currentGame;
    }

    public static GameBoard getGameBoard(){
        return HearthStone.currentGameBoard;
    }

    public static int getWhoseTurn() {
        return getGame().getWhoseTurn();
    }

    public static ArrayList<Card> getLand(int playerId) {
        return getPlayer(playerId).getLand();
    }

    public static ArrayList<Card> getHand(int playerId) {
        return getPlayer(playerId).getHand();
    }

    public static ArrayList<MinionCard> getNeighbors(int playerId, Card card) {
        return getPlayer(playerId).neighborCards(card);
    }

    public static Deck getDeck(int playerId) {
        return getPlayer(playerId).getDeck();
    }

    public static int getMana(int playerId) {
        return getPlayer(playerId).getMana();
    }

    public static int getMana(Card card) {
        return card.getManaCost();
    }

    public static int getTurnNumber(int playerId) {
        return getPlayer(playerId).getTurnNumber();
    }

    public static Passive getPassive(int playerId) {
        return getPlayer(playerId).getPassive();
    }

    public static HeroPowerCard getHeroPower(int playerId) {
        return getPlayer(playerId).getHeroPower();
    }

    public static WeaponCard getWeapon(int playerId) {
        return getPlayer(playerId).getWeapon();
    }

    public static RewardCard getReward(int playerId) {
        return getPlayer(playerId).getReward();
    }

    public static Hero getHero(int playerId) {
        return getPlayer(playerId).getHero();
    }

    public static String getPlayerName(int playerId){
        return getPlayer(playerId).getUsername();
    }

    public static int spentManaOnMinions(int playerId) {
        return getPlayer(playerId).getManaSpentOnMinions();
    }

    public static int spentManaOnSpells(int playerId) {
        return getPlayer(playerId).getManaSpentOnSpells();
    }

    public static int getMaxManaInGame() {
        return GameConfigs.maxManaInGame;
    }

    public static int getNumberOfPassive() {
        return GameConfigs.initialPassives;
    }

    public static ArrayList<Card> getTopCards(int playerId, int numberOfTopCards){
        return getPlayer(playerId).getTopCards(numberOfTopCards);
    }

    public static boolean isLost(int playerId){
        return getPlayer(playerId).getHero().getHealth() <= 0;
    }

    public static boolean haveTaunt(int playerId){
        return getPlayer(playerId).haveTaunt();
    }

    public static int getEnemyId(int playerId){
        if(playerId == 0)
            return 1;
        return 0;
    }

    public static Card getRandomCardFromOriginalDeck(int playerId, CardType cardType){
        return getPlayer(playerId).getFactory().getRandomCardFromOriginalDeck(cardType);
    }

    public static MinionCard getRandomMinionFromLand(int playerId){
        return getPlayer(playerId).getFactory().getRandomMinionFromLand();
    }

    public static Card getRandomCardFromHand(int playerId){
        return getPlayer(playerId).getFactory().getRandomCardFromHand();
    }

    public static Card getRandomCardFromCurrentDeck(int playerId){
        return getPlayer(playerId).getFactory().getRandomCardFromCurrentDeck();
    }
    public static int getHealth(Character character){
        return character.getHealth();
    }

    public static Passive getBasePassive(int ind){
        return HearthStone.basePassives.get(ind);
    }

    public static boolean haveWeapon(int playerId){
        return getPlayer(playerId).haveWeapon();
    }

    public static boolean isSpellSafe(MinionCard minionCard){
        return minionCard.isSpellSafe();
    }

    public static Card deckCard(int playerId, int ind){
        return getPlayer(playerId).getDeck().getCards().get(ind);
    }
}
