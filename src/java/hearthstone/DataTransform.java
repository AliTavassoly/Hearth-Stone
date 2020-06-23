package hearthstone;

import hearthstone.models.Deck;
import hearthstone.models.Passive;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.player.Player;

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

    public ArrayList<Card> getLand(Player player){
        return player.getLand();
    }

    public ArrayList<Card> getHand(Player player){
        return player.getHand();
    }

    public Deck getDeck(Player player){
        return player.getDeck();
    }

    public int getMana(Player player){
        return player.getMana();
    }

    public int getTurnNumber(Player player){
        return player.getTurnNumber();
    }

    public Passive getPassive(Player player){
        return player.getPassive();
    }

    public void setPassive(Player player, Passive passive){
        player.setPassive(passive);
    }

    public HeroPowerCard getHeroPower(Player player){
        return player.getHeroPower();
    }

    public WeaponCard getWeapon(Player player){
        return player.getWeapon();
    }
}
