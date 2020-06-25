package hearthstone;

import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
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

    public void damage(int damage, MinionCard minionCard){
        minionCard.setHealth(minionCard.getHealth() - damage);
    }

    public void damage(int damage, Hero hero){
        hero.setHealth(hero.getHealth() - damage);
    }

    public void heal(int heal, MinionCard minionCard){
        minionCard.setHealth(minionCard.getHealth() + heal);
    }

    public void heal(int heal, Hero hero){
        hero.setHealth(hero.getHealth() + heal);
    }

    public void setHealth(int health, MinionCard minionCard){
        minionCard.setHealth(health);
    }

    public void addAttack(int attack, MinionCard minionCard){ minionCard.setAttack(minionCard.getAttack() + attack); }

    public void addDurability(int durability, WeaponCard weaponCard){ weaponCard.setDurability(weaponCard.getDurability() + durability); }

    public void addAttack(int attack, WeaponCard weaponCard){ weaponCard.setAttack(weaponCard.getAttack() + attack); }

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
