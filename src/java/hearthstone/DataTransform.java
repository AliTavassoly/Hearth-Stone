package hearthstone;

import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;

import java.util.ArrayList;

public class DataTransform {
    private static DataTransform instance;

    private DataTransform(){
    }

    public static DataTransform getInstance(){
        if(instance == null){
            return instance = new DataTransform();
        } else {
            return instance;
        }
    }

    public int getWhoseTurn(){
        return HearthStone.currentGame.getWhoseTurn();
    }

    public ArrayList<Card> getLand(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getLand();
    }

    public ArrayList<Card> getHand(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getHand();
    }

    public Deck getDeck(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getDeck();
    }

    public int getMana(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getMana();
    }

    public int getTurnNumber(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getTurnNumber();
    }

    public Passive getPassive(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getPassive();
    }

    public void setPassive(int playerId, Passive passive){
        HearthStone.currentGame.getPlayerById(playerId).setPassive(passive);
    }

    public HeroPowerCard getHeroPower(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getHeroPower();
    }

    public WeaponCard getWeapon(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getWeapon();
    }

    public Hero getHero(int playerId){
        return HearthStone.currentGame.getPlayerById(playerId).getHero();
    }

    public int getMaxManaInGame(){
        return GameConfigs.maxManaInGame;
    }

    public int getNumberOfPassive(){
        return GameConfigs.initialPassives;
    }
}
