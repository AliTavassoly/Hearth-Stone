package hearthstone;

import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

public class Mapper {
    private static Mapper instance;

    private Mapper(){
    }

    public static Mapper getInstance(){
        if(instance == null){
            return instance = new Mapper();
        } else {
            return instance;
        }
    }

    public void playCard(int playerId, Card card) throws Exception{
        HearthStone.currentGame.getPlayerById(playerId)
                .playCard(card);
    }

    public void doPassive(int playerId){
        HearthStone.currentGame.getPlayerById(playerId)
                .doPassives();
    }

    public void endTurn(){
        HearthStone.currentGame.endTurn();
    }

    public void startGame(){ HearthStone.currentGame.startGame();}

    public void damage(int damage, MinionCard minionCard) throws HearthStoneException {
        minionCard.gotDamage(damage);
        updateBoard();
    }

    public void damage(int damage, Hero hero) throws HearthStoneException{
        hero.gotDamage(damage);
        updateBoard();
    }

    public void heal(int heal, MinionCard minionCard) throws HearthStoneException{
        minionCard.gotHeal(heal);
        updateBoard();
    }

    public void heal(int heal, Hero hero)throws HearthStoneException {
        hero.gotHeal(heal);
        updateBoard();
    }

    public void setHealth(int health, MinionCard minionCard){
        minionCard.setHealth(health);
        updateBoard();
    }

    public void addAttack(int attack, MinionCard minionCard) {
        minionCard.changeAttack(attack);
        updateBoard();
    }

    public void restoreHealth(int heal, MinionCard minionCard){
        minionCard.restoreHeal(heal);
    }

    public void restoreHealth(int heal, Hero hero){
        hero.restoreHealth(heal);
    }

    public void setPassive(int playerId, Passive passive) {
        HearthStone.currentGame.getPlayerById(playerId).setPassive(passive);
    }

    public void restartSpentManaOnMinions(int playerId) {
        HearthStone.currentGame.getPlayerById(playerId).setManaSpentOnMinions(0);
    }

    public void restartSpentManaOnSpells(int playerId) {
        HearthStone.currentGame.getPlayerById(playerId).setManaSpentOnSpells(0);
    }

    public void foundObjectForObject(Object waited, Object founded) throws HearthStoneException {
        if(waited instanceof Card){
            Card card = (Card)waited;
            card.found(founded);
        }
    }

    public void deleteCurrentMouseWaiting(){
        HearthStone.currentGameBoard.deleteCurrentMouseWaiting();
    }

    public void makeNewMouseWaiting(CursorType cursorType, Card card){
        HearthStone.currentGameBoard.makeNewMouseWaiting(cursorType, card);
    }

    public void updateBoard(){
        HearthStone.currentGame.getPlayerById(0).updatePlayer();
        HearthStone.currentGame.getPlayerById(1).updatePlayer();
        HearthStone.currentGameBoard.restart();
    }
}
